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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openo.sdno.sptndriver.convertor.L2Convertor;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.services.SElineServices;

@Path("/openoapi/sbi-l2vpn-vpws/v1")
@Produces(MediaType.APPLICATION_JSON)

public class L2Resource {

  private final Validator validator;

  public L2Resource(Validator validator) {
    this.validator = validator;
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
    SElineServices elineServices = new SElineServices();
    try {
      elineServices.createElineAndTunnels(createElineAndTunnels);
    } catch (HttpErrorException e) {
      e.printStackTrace();
      return e.getResponse();
    } catch (IOException e) {
      e.printStackTrace();
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    } catch (CommandErrorException e) {
      e.printStackTrace();
      return e.getResponse();
    }
    return Response.created(new
                                URI(String.valueOf(L2))).build();
  }

  @DELETE
  public Response deleteEline(String ElineId) throws URISyntaxException {
    String sElineId = L2Convertor.getSouthElineId(ElineId);
    if (sElineId == null) {
      return Response
          .status(Response.Status.BAD_REQUEST)
          .entity("Can not find Eline.")
          .build();
    }
    SElineServices elineServices = new SElineServices();
    try {
      elineServices.deleteEline(sElineId);
    } catch (HttpErrorException e) {
      e.printStackTrace();
      return e.getResponse();
    } catch (IOException e) {
      e.printStackTrace();
      return Response
          .status(Response.Status.BAD_GATEWAY)
          .entity("IO Exception when creating Eline.")
          .build();
    }
    // todo: API required to return whole Eline Info
    return Response.noContent().build();
  }
}
