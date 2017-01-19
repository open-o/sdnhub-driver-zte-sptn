/*
 * Copyright 2016-2017 ZTE Corporation.
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

package org.openo.sdnhub.sptndriver.simulator.resouces;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openo.sdnhub.sptndriver.utils.JsonUtil;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The class to execute eline CRUD.
 */
@Path("/restconf/operations/")
@Produces(MediaType.APPLICATION_JSON)
public class ElineResource {

    /**
     * Create an eline and related tunnels.
     *
     * @param eline eline information.
     * @return 201 if success or 204 if failed.
     */
    @POST
    @Path("/sptn-service-eline:create-snc-eline-tunnels")
    public Response createElineAndTunnels(Object eline) {
        Object output;
        try {
            output = JsonUtil.readJsonFromFile("./conf/json/create_eline_and_tunnels.json");
        } catch (Exception ex) {
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ExceptionUtils.getStackTrace(ex))
                .type(MediaType.TEXT_PLAIN_TYPE)
                .build();
        }
        return Response
            .status(Response.Status.CREATED)
            .entity(output)
            .build();
    }

    /**
     * Delete an eline.
     *
     * @param eline eline info
     * @return 204
     */
    @POST
    @Path("/sptn-service-eline:delete-snc-eline")
    public Response deleteEline(Object eline) {
        return Response.noContent().build();
    }
}
