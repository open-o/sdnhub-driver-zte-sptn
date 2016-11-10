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

package org.openo.sdno.sptndriver.services;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The class to send command to driver manager.
 */
public class DriverManagerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerService.class);

  private String msbUrl;

  public DriverManagerService(String msbUrl) {
    this.msbUrl = msbUrl;
  }

  /**
   *  Register driver to driver manager.
   * @param driverInfo Information of driver.
   */
  public boolean registerDriver(Object driverInfo) {
    LOGGER.debug("Register sptn driver begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(msbUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    DriverManagerServiceInterface service = retrofit.create(DriverManagerServiceInterface.class);
    Call<ResponseBody> cmdCall = service.registerDriver(driverInfo);
    Response<ResponseBody> response;
    try {
      response = cmdCall.execute();
    } catch (IOException ex) {
      LOGGER.error("Register sptn driver failed, due to: " + ExceptionUtils.getStackTrace(ex));
      return false;
    }

    try {
      ServiceUtil.parseResponse(response, LOGGER, "Register sptn driver");
    } catch (CommandErrorException ex) {
      LOGGER.error("Register sptn driver failed, due to: " + ExceptionUtils.getStackTrace(ex));
      return false;
    } catch (HttpErrorException ex) {
      if (ex.getResponse().getStatus() == RegisterStatus.INVALID_PARAMETER.status) {
        LOGGER.warn("Invalid parameter or register twice.");
        return true;
      } else {
        LOGGER.error("Register sptn driver failed: Internal server error, error code is: "
            + ex.getResponse().getStatus());
        return false;
      }
    }

    LOGGER.debug("Register sptn driver end. ");
    return true;
  }

  private enum RegisterStatus {
    INVALID_PARAMETER(415),
    INTERNAL_SERVER_ERROR(500);
    private int status;
    RegisterStatus(int status) {
      this.status = status;
    }
  }

  /**
   *  Unregister driver to driver manager.
   * @param driverInstanceId Instance id of driver.
   */
  public boolean unregisterDriver(String driverInstanceId) {
    LOGGER.debug("Unregister sptn driver  begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(msbUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    DriverManagerServiceInterface service = retrofit.create(DriverManagerServiceInterface.class);
    Call<ResponseBody> cmdCall = service.unregisterDriver(driverInstanceId);
    Response<ResponseBody> response;
    try {
      response = cmdCall.execute();
    } catch (IOException ex) {
      LOGGER.error("Unregister sptn driver command execute failed, due to: "
          + ExceptionUtils.getStackTrace(ex));
      return false;
    }

    try {
      ServiceUtil.parseResponse(response, LOGGER, "Unregister sptn driver");
    } catch (Exception ex) {
      LOGGER.error("Unregister sptn driver parse response failed, due to: "
          + ExceptionUtils.getStackTrace(ex));
      return false;
    }

    LOGGER.debug("Unregister sptn driver end. ");
    return true;
  }
}
