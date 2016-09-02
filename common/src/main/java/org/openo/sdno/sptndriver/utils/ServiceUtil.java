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

package org.openo.sdno.sptndriver.utils;

import org.openo.sdno.sptndriver.enums.south.SCmdResultStatus;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCommandResult;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.slf4j.Logger;

import retrofit2.Response;

/**
 * Util class for REST commands processing services.
 */
public class ServiceUtil {

  /**
   * Parse the result of REST commands that the return type is SCommandResultOutput.
   *
   * @param response Response of execution.
   * @throws CommandErrorException When command result is not successful.
   * @throws HttpErrorException    When receives http error.
   */
  public static void parseCommandResultOutput(Response<SCommandResultOutput> response,
                                              Logger LOGGER,
                                              String printText)
      throws CommandErrorException, HttpErrorException {
    if (response.isSuccessful()) {
      if (response.body() == null || response.body().getOutput() == null) {
        LOGGER.info(printText + " successfully, but response is null.");
        return;
      }
      SCommandResult commandResult = response.body().getOutput();
      if (commandResult == null || commandResult.getResult() == null) {
        LOGGER.info(printText + " successfully, but command result is null.");
        return;
      }
      if (commandResult.getResult() != null) {
        if (commandResult.getResult()
            .equals(Integer.getInteger(SCmdResultStatus.SUCCESS.toString()))) {
          LOGGER.debug(printText + " successfully. ");
          return;
        } else {
          LOGGER.error(printText + " failed, command result error.");
          throw new CommandErrorException(commandResult);
        }
      }
    } else {
      LOGGER.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  /**
   * Parse the result of REST commands that return no data, the use of SCommandResultOutput is only
   * to avoid use callback function.
   *
   * @throws HttpErrorException When command execution FAILED.
   */
  public static void parseRPCResult(Response<SCommandResultOutput> response,
                                    Logger LOGGER,
                                    String printText)
      throws HttpErrorException {
    if (response.isSuccessful()) {
      LOGGER.debug(printText + " successfully. ");
    } else {
      LOGGER.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  /**
   * Parse response.
   *
   * @param response  http response.
   * @param LOGGER    Log information.
   * @param printText Text print in the log.
   * @param <T>       Body of response.
   */
  public static <T> T parseResponse(Response<T> response,
                                    Logger LOGGER,
                                    String printText)
      throws CommandErrorException, HttpErrorException {
    if (response.isSuccessful()) {
      return response.body();
    } else {
      LOGGER.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }
}
