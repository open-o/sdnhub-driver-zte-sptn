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

package org.openo.sdno.sptndriver.exception;

import org.openo.sdno.sptndriver.models.south.SCommandResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Exception when the command result from controller is failed.
 */
public class CommandErrorException extends ServerException {

    private final String errorInfo;

    public CommandErrorException(final SCommandResult cmdResult) {
        errorInfo = getErrorInfo(cmdResult);
    }

    private static String getErrorInfo(final SCommandResult cmdResult) {
        String errorInfo;
        if (cmdResult != null
            && cmdResult.getFailedResources() != null
            && !cmdResult.getFailedResources().getFailedResourceList().isEmpty()) {
            errorInfo = "Controller returns failure: "
                + cmdResult.getFailedResources().getFailedResourceList().get(0).getErrorMessage();
        } else {
            errorInfo = "Error information from controller is null.";
        }
        return errorInfo;
    }

    @Override
    public String toString() {
        return errorInfo;
    }

    @Override
    public Response getResponse() {
        return Response
            .status(Response.Status.NOT_IMPLEMENTED)
            .type(MediaType.TEXT_PLAIN_TYPE)
            .entity(toString())
            .build();
    }

}
