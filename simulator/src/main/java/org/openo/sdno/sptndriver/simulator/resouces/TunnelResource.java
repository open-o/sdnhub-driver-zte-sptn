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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openo.sdno.sptndriver.utils.JsonUtil;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class for tunnel CRUD.
 */
@Path("/restconf/operations/")
@Produces(MediaType.APPLICATION_JSON)
public class TunnelResource {
    /**
     * Calculate tunnel route
     *
     * @param input Tunnel route calculation requirement.
     * @return 200
     */
    @Path("/sptn-service-route:request-routes")
    @POST
    public Response calculateRoute(Object input) {
        Object output;
        try {
            output = JsonUtil.readJsonFromFile("./conf/json/calculate_route.json");
        } catch (Exception ex) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ExceptionUtils.getStackTrace(ex))
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
        }
        return Response.ok(output).build();
    }
}
