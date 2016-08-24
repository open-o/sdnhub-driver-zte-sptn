/*
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.sdno.sptndriver.resources;

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.convertor.L2Convertor;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NDeleteL2vpn;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SDeleteEline;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.services.SElineServices;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/openoapi/sbi-l2vpn-vpws/v1")
@Produces(MediaType.APPLICATION_JSON)

public class L2Resource {

  private final Validator validator;
  private Config config;

  public L2Resource(Validator validator, Config config) {
    this.validator = validator;
    this.config = config;
  }

  @POST
  public Response createEline(NL2Vpn L2) throws URISyntaxException {
    SCreateElineAndTunnels createElineAndTunnels = L2Convertor.L2ToElineTunnerCreator(L2);
    if (createElineAndTunnels == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Input L2 can not be converted to Eline.")
          .build();
    }
    SElineServices elineServices = new SElineServices(config.getControllerUrl());
    try {
      elineServices.createElineAndTunnels(createElineAndTunnels);
    } catch (HttpErrorException e) {
      return e.getResponse();
    } catch (IOException e) {
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    } catch (CommandErrorException e) {
      return e.getResponse();
    }
    return Response.created(new
                                URI(String.valueOf(L2))).build();
  }

  @DELETE
  public Response deleteEline(NDeleteL2vpn ElineId) throws URISyntaxException {
    String sElineId = L2Convertor.getSouthElineId(ElineId.getVpnid());
    if (sElineId == null || ElineId.getVpnid() == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find Eline.")
          .build();
    }
    SDeleteElineInput elineDeleteInput = new SDeleteElineInput();
    SDeleteEline deleteEline = new SDeleteEline();
    deleteEline.setElineId(ElineId.getVpnid());
    elineDeleteInput.setInput(deleteEline);

    SElineServices elineServices = new SElineServices(config.getControllerUrl());
    try {
      elineServices.deleteEline(elineDeleteInput);
    } catch (HttpErrorException e) {
      return e.getResponse();
    } catch (CommandErrorException e) {
      return e.getResponse();
    } catch (IOException e) {
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    }
    // todo: API required to return whole Eline Info
    return Response.noContent().build();
  }
}
