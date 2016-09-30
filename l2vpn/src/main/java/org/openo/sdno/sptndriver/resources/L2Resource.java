/*
 * Copyright 2016 ZTE Corporation.
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
import org.openo.sdno.sptndriver.db.dao.UuidMapDao;
import org.openo.sdno.sptndriver.db.model.UuidMap;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SDeleteEline;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.services.SElineServices;
import org.openo.sdno.sptndriver.services.STunnelServices;
import org.openo.sdno.sptndriver.utils.EsrUtil;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.skife.jdbi.v2.DBI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
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
  private final UuidMapDao uuidMapDao;
  private Config config;

  /**
   * The constructor.
   *
   * @param validator validation parameter.
   * @param config    Configurations read from configuration file.
   */
  public L2Resource(Validator validator, Config config, DBI jdbi) {
    this.validator = validator;
    this.config = config;
    this.uuidMapDao = jdbi.onDemand(UuidMapDao.class);
  }

  /**
   * The post method to create Eline.
   *
   * @param l2vpn Parameter of create L2vpn.
   * @return 200 if success
   */
  @POST
  public Response createEline(NL2Vpn l2vpn,
                              @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    String controllerId = ServiceUtil.getControllerId(controllerIdPara);
    SRouteCalReqsInput routeCalInput = SRouteCalReqsInitiator.initElineLspCalRoute(l2vpn);
    String externalId;
    try {
      STunnelServices tunnelServices = new STunnelServices(
          (EsrUtil.getSdnoControllerUrl(controllerId, config)));
      SCreateElineAndTunnelsInput createElineAndTunnels
          = L2Converter.convertL2ToElineTunnerCreator(l2vpn);
      if (createElineAndTunnels == null || routeCalInput == null) {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity("Input L2 can not be converted to Eline.")
            .build();
      }
      // Calculate LSP route first.
      createElineAndTunnels.getInput().setRouteCalResults(
          tunnelServices.calcRoutes(routeCalInput).getOutput().getRouteCalResults());
      // Create Eline.
      SElineServices elineServices = new SElineServices(
          EsrUtil.getSdnoControllerUrl(controllerId, config));
      externalId = elineServices.createElineAndTunnels(createElineAndTunnels);
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

    uuidMapDao.insert(l2vpn.getId(), externalId, UuidMap.UuidTypeEnum.ELINE.name(), controllerId);
    return Response.status(Response.Status.CREATED)
        .entity(l2vpn).build();
  }

  /**
   * The delete method to delete Eline.
   *
   * @param vpnid Global UUID of Eline(UUID in SDN-O).
   * @return 200 if success
   */
  @DELETE
  @Path("{vpnid}")
  public Response deleteEline(@PathParam("vpnid") String vpnid,
                              @HeaderParam("X-Driver-Parameter") String controllerIdPara)
      throws URISyntaxException {
    String controllerId = ServiceUtil.getControllerId(controllerIdPara);
    String southElineId = getSouthElineId(vpnid, controllerId);
    if (southElineId == null || vpnid == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find Eline.")
          .build();
    }
    SDeleteElineInput elineDeleteInput = new SDeleteElineInput();
    SDeleteEline deleteEline = new SDeleteEline();
    deleteEline.setElineId(southElineId);
    elineDeleteInput.setInput(deleteEline);


    try {
      SElineServices elineServices = new SElineServices(
          EsrUtil.getSdnoControllerUrl(controllerId, config));
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
    uuidMapDao.delete(vpnid, UuidMap.UuidTypeEnum.ELINE.name(), controllerId);
    // todo: API required to return whole Eline Info
    return Response.ok().build();
  }

  private String getSouthElineId(String uuid, String controllerId) {
    return uuidMapDao.get(uuid, UuidMap.UuidTypeEnum.ELINE.name(),
        controllerId).getExternalId();
  }

}
