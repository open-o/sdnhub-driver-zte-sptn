/*
 * Copyright 2016 ZTE, Inc. and others.
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

import javax.ws.rs.core.Response;

/**
 *  Exception when the command result from controller is failed.
 */
public class CommandErrorException extends Exception {

  private SCommandResult m_cmdResult;

  public CommandErrorException(SCommandResult cmdResult) {
    m_cmdResult = cmdResult;
  }

  /**
   *  Get response constructed from command result of controller.
   * @return Response which is returned to LCM.
   */
  public Response getResponse() {
    String errorMsg = null;
    if (m_cmdResult != null && m_cmdResult.getFailedResources() != null) {
      errorMsg = m_cmdResult.getFailedResources().getErrorMessage();
    } else {
      errorMsg = "Command Result is null.";
    }
    return Response
        .status(Response.Status.NOT_IMPLEMENTED)
        .entity(errorMsg)
        .build();
  }
}
