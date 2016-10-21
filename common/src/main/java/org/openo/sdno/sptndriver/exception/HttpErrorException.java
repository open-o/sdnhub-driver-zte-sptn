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

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import retrofit2.Response;

/**
 *  The exception happened when the controller returns http error.
 */
public class HttpErrorException extends Exception {

  private Response response;

  public HttpErrorException(Response response) {
    this.response = response;
  }

  /**
   *  Get the response constructed from the http error.
   * @return  Response which is returned to LCM.
   */
  public javax.ws.rs.core.Response getResponse() {

    String errorStr = "unknown error";
    try {
      errorStr = response.errorBody().string();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return javax.ws.rs.core.Response
        .status(response.code())
        .entity(errorStr)
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build();

  }
}
