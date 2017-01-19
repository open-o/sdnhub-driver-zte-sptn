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

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Wrap IOException to construct Response.
 */
public class ServerIoException extends ServerException {

    private final java.io.IOException ex;

    public ServerIoException(final IOException ex) {
        this.ex = ex;
    }

    @Override
    public Response getResponse() {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(ex.toString())
            .type(MediaType.TEXT_PLAIN_TYPE)
            .build();
    }
}
