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

package org.openo.sdno.sptndriver.simulator.resouces;

import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *  The class to execute L3vpn CRUD.
 */
@Path("/restconf/data/sptn-service-l3vpn:service/")
@Produces(MediaType.APPLICATION_JSON)
public class L3Resource {

  /**
   *  Create l3vpn.
   * @param l3vpn L3vpn information.
   * @return success or failed.
   */
  @POST
  @Path("/snc-l3vpns")
  public Response createL3vpn(Object l3vpn) {
    return Response.ok(new SCommandResultOutput()).build();
  }

  /**
   *  Delete l3vpn
   * @param id UUID of l3vpn to be deleted.
   * @return 204
   */
  @POST
  @Path("/snc-l3vpns/snc-l3vpn={id}")
  public Response deleteL3vpn(@PathParam("id") String id) {
    return Response.noContent().build();
  }
}
