/*
 * Copyright 2016 ZTE, Inc. and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.sptndriver.resources;

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.converter.L2Converter;
import org.openo.sdno.sptndriver.converter.SRouteCalReqsInitiator;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SDeleteEline;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.services.SElineServices;
import org.openo.sdno.sptndriver.services.STunnelServices;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class to provide L2vpn resource.
 */
@Path("/openoapi/sbi-l2vpn-vpws/v1/")
@Produces(MediaType.APPLICATION_JSON)
public class L2Resource {

  private final Validator validator;
  private Config config;

  /**
   * The constructor.
   *
   * @param validator validation parameter.
   * @param config    Configurations read from configuration file.
   */
  public L2Resource(Validator validator, Config config) {
    this.validator = validator;
    this.config = config;
  }

  /**
   * The post method to create Eline.
   *
   * @param l2vpn Parameter of create L2vpn.
   * @return 200 if success
   */
  @POST
  public Response createEline(NL2Vpn l2vpn) throws URISyntaxException {
    SRouteCalReqsInput routeCalInput = SRouteCalReqsInitiator.initElineLspCalRoute(l2vpn);
    STunnelServices tunnelServices = new STunnelServices(config.getControllerUrl());
    SCreateElineAndTunnelsInput createElineAndTunnels
        = L2Converter.convertL2ToElineTunnerCreator(l2vpn);
    if (createElineAndTunnels == null || routeCalInput == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Input L2 can not be converted to Eline.")
          .build();
    }

    try {
      // Calculate LSP route first.
      createElineAndTunnels.getInput().setRouteCalResults(
          tunnelServices.calcRoutes(routeCalInput).getOutput().getRouteCalResults());
      // Create Eline.
      SElineServices elineServices = new SElineServices(config.getControllerUrl());
      elineServices.createElineAndTunnels(createElineAndTunnels);
    } catch (HttpErrorException ex) {
      return ex.getResponse();
    } catch (IOException ex) {
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    } catch (CommandErrorException ex) {
      return ex.getResponse();
    }
    return Response.created(new URI(String.valueOf(l2vpn))).build();
  }

  /**
   * The delete method to delete Eline.
   *
   * @param vpnid Global UUID of Eline(UUID in SDN-O).
   * @return 200 if success
   */
  @DELETE
  @Path("{vpnid}")
  public Response deleteEline(@PathParam("vpnid") String vpnid) throws URISyntaxException {
    String southElineId = L2Converter.getSouthElineId(vpnid);
    if (southElineId == null || vpnid == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find Eline.")
          .build();
    }
    SDeleteElineInput elineDeleteInput = new SDeleteElineInput();
    SDeleteEline deleteEline = new SDeleteEline();
    deleteEline.setElineId(vpnid);
    elineDeleteInput.setInput(deleteEline);

    SElineServices elineServices = new SElineServices(config.getControllerUrl());
    try {
      elineServices.deleteEline(elineDeleteInput);
    } catch (HttpErrorException ex) {
      return ex.getResponse();
    } catch (CommandErrorException ex) {
      return ex.getResponse();
    } catch (IOException ex) {
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    }
    // todo: API required to return whole Eline Info
    return Response.noContent().build();
  }
}
