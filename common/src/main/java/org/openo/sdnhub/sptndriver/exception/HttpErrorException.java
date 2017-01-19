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

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import retrofit2.Response;

/**
 * The exception happened when the controller returns HTTP error.
 */
public class HttpErrorException extends ServerException {

    private final String errorInfo;
    private final int errorCode;

    public HttpErrorException(final Response response) {
        errorInfo = getErrorInfo(response);
        errorCode = response.code();
    }

    private static String getErrorInfo(final Response response) {
        String errorInfo = "Controller returns unsuccessful status: ";
        try {
            errorInfo += response.errorBody().string();
        } catch (IOException ex) {
            errorInfo += ExceptionUtils.getStackTrace(ex);
        }
        return errorInfo;
    }

    @Override
    public String toString() {
        return errorInfo;
    }

    @Override
    public javax.ws.rs.core.Response getResponse() {
        return javax.ws.rs.core.Response
            .status(errorCode)
            .entity(toString())
            .type(MediaType.TEXT_PLAIN_TYPE)
            .build();
    }
}
