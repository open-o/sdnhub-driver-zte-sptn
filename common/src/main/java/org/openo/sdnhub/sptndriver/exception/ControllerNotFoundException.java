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

package org.openo.sdnhub.sptndriver.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * When can not find controller, throw this exception.
 */
public class ControllerNotFoundException extends ServerException {
    private final String errorInfo;

    public ControllerNotFoundException(final Exception ex, final String controllerId) {
        errorInfo = "Can not find controller: " + controllerId
            + " due to: " + ex.toString();
    }

    @Override
    public String toString() {
        return errorInfo;
    }

    @Override
    public Response getResponse() {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(toString())
            .build();
    }
}
