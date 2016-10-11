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

package org.openo.sdno.sptndriver.utils;

import org.openo.sdno.sptndriver.enums.south.SCmdResultStatus;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdno.sptndriver.models.south.SCommandResult;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Response;

/**
 * Utility class for REST commands processing services.
 */
public class ServiceUtil {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ServiceUtil.class);

  /**
   * Parse the result of REST commands that the return type is SCommandResultOutput.
   *
   * @param response Response of execution.
   * @throws CommandErrorException When command result is not successful.
   * @throws HttpErrorException    When receives HTTP error.
   */
  public static void parseCommandResultOutput(Response<SCommandResultOutput> response,
                                              Logger logger,
                                              String printText)
      throws CommandErrorException, HttpErrorException {
    if (response.isSuccessful()) {
      if (response.body() == null || response.body().getOutput() == null) {
        logger.info(printText + " successfully, but response is null.");
        return;
      }
      SCommandResult commandResult = response.body().getOutput();
      checkSuccess(response, commandResult, logger,printText);
    } else {
      logger.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  /**
   * Parse the result of REST commands that the return type is SCmdResultAndNcdResRelationsOutput.
   *
   * @param response Response of execution.
   * @throws CommandErrorException When command result is not successful.
   * @throws HttpErrorException    When receives HTTP error.
   */
  public static void parseCmdResultAndNcdResRelOutput(
      Response<SCmdResultAndNcdResRelationsOutput> response,
      Logger logger,
      String printText)
      throws CommandErrorException, HttpErrorException {
    if (response.isSuccessful()) {
      if (response.body() == null || response.body().getOutput() == null) {
        logger.info(printText + " successfully, but response is null.");
        return;
      }
      SCmdResultAndNcdResRelationsOutput commandResultOutput = response.body();
      if (commandResultOutput == null
          || commandResultOutput.getOutput() == null) {
        logger.info(printText + " successfully, but command result output is null.");
        return;
      }
      SCommandResult commandResult = commandResultOutput.getOutput().getCommandResult();
      checkSuccess(response, commandResult, logger, printText);
    } else {
      logger.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  /**
   * Parse the result of REST commands that return no data, the use of SCommandResultOutput is only
   * to avoid use callback function.
   *
   * @throws HttpErrorException When command execution FAILED.
   */
  public static void parseRpcResult(Response<SCommandResultOutput> response,
                                    Logger logger,
                                    String printText)
      throws HttpErrorException {
    if (response.isSuccessful()) {
      logger.debug(printText + " successfully. ");
    } else {
      logger.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  /**
   * Parse response.
   *
   * @param response  HTTP response.
   * @param logger    Log information.
   * @param printText Text print in the log.
   * @param <T>       Body of response.
   */
  public static <T> T parseResponse(Response<T> response,
                                    Logger logger,
                                    String printText)
      throws CommandErrorException, HttpErrorException {
    if (response.isSuccessful()) {
      return response.body();
    } else {
      logger.error(printText + " failed, response unsuccessful.");
      throw new HttpErrorException(response);
    }
  }

  private static <T> void checkSuccess(Response<T> response,
                                       SCommandResult commandResult,
                                       Logger logger,
                                       String printText)
      throws CommandErrorException, HttpErrorException {
    if (commandResult == null || commandResult.getResult() == null) {
      logger.info(printText + " successfully, but command result is null.");
      return;
    }
    if (commandResult.getResult() != null) {
      if (commandResult.getResult().equals(SCmdResultStatus.SUCCESS.toString())) {
        logger.debug(printText + " successfully. ");
      } else {
        logger.error(printText + " failed, command result error.");
        throw new CommandErrorException(commandResult);
      }
    }
    return;
  }

  /**
   *  Parse controller id.
   * @param controllerIdPara Controller id param,“extSysID={ctrlUuid}”
   * @return Controller id, "{ctrlUuid}", if failed, return null.
   */
  public static String getControllerId(String controllerIdPara) {
    String[] strArray = controllerIdPara.split("=");
    if (strArray.length == 2) {
      return strArray[1];
    }
    LOGGER.error("getControllerId() must have one '='.");
    return null;
  }

}
