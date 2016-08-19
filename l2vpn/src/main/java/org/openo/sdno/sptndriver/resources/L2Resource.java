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

import org.openo.sdno.sptndriver.models.north.NL2Vpn;

@Path("/sptndriver/v1/nbi-eline")
@Produces(MediaType.APPLICATION_JSON)

public class L2Resource {

  private final Validator validator;

  public L2Resource(Validator validator) {
    this.validator = validator;
  }

  @POST
  public Response createEline(NL2Vpn L2) throws URISyntaxException {

    return Response.created(new
                                URI(String.valueOf(new NL2Vpn()))).build();

  }

  @GET
  @Path("/snc-eline={id}")
  public Response getContact(@PathParam("id") int id) throws URISyntaxException {
    return Response.created(new
                                URI(String.valueOf(new NL2Vpn()))).build();
  }

  @DELETE
  public Response deleteEline(String ElineId) throws URISyntaxException {
    return Response.created(new
                                URI(String.valueOf(new NL2Vpn()))).build();
  }
}
