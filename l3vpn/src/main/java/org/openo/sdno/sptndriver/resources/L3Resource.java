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
import org.openo.sdno.sptndriver.converter.L3Converter;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NL3Vpn;
import org.openo.sdno.sptndriver.models.south.SL3vpn;
import org.openo.sdno.sptndriver.services.L3Service;

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

@Path("/openoapi/sbi-l3vpn/v1")
@Produces(MediaType.APPLICATION_JSON)
/**
 *  The class of L3vpn resource.
 */
public class L3Resource {

  private final Validator validator;

  private Config config;

  public L3Resource(Validator validator, Config config) {
    this.validator = validator;
    this.config = config;
  }

  /**
   * Create L3vpn.
   *
   * @param l3vpn L3vpn parameters.
   * @return 200 if success.
   */
  @POST
  @Path("/l3vpns")
  public javax.ws.rs.core.Response createL3vpn(NL3Vpn l3vpn) throws URISyntaxException {
    SL3vpn southL3vpn = L3Converter.convertNbiToSbi(l3vpn);
    if (southL3vpn == null || southL3vpn.getAcList() == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Input L3vpn can not be converted to south L3vpn.")
          .build();
    }

    try {
      L3Service l3Service = new L3Service(config.getControllerUrl());
      l3Service.createL3vpn(southL3vpn);
    } catch (HttpErrorException ex) {
      return ex.getResponse();
    } catch (IOException ex) {
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating L3vpn.")
          .build();
    } catch (CommandErrorException ex) {
      return ex.getResponse();
    }
    return Response.created(new URI(String.valueOf(l3vpn))).build();

  }

  /**
   * Delete L3vpn.
   *
   * @param vpnid Global UUID of L3vpn(UUID of L3vpn in SDN-O).
   * @return 200 if success
   */
  @DELETE
  @Path("/l3vpns/{vpnid}")
  public Response deleteL3vpn(@PathParam("vpnid") String vpnid) throws URISyntaxException {
    String southL3vpnId = L3Converter.getSouthL3vpnId(vpnid);
    if (southL3vpnId == null || vpnid == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find L3vpn.")
          .build();
    }

    L3Service l3Service = new L3Service(config.getControllerUrl());
    try {
      l3Service.deleteL3vpn(southL3vpnId);
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
    // TODO: 2016/9/13
    return Response.noContent().build();
  }
}
