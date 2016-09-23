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

import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.openo.sdno.sptndriver.models.south.SL3vpn;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * L3vpn service CRUD.
 */
public class L3Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(L3Service.class);

  private String baseUrl;

  public L3Service(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Send REST to controller to create L3vpn.
   *
   * @param l3vpn L3vpn information
   */
  public void createL3vpn(SL3vpn l3vpn)
      throws HttpErrorException, IOException, CommandErrorException {
    String printText = "Create l3vpn " + l3vpn.getId();
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    L3ServiceInterface service = retrofit.create(L3ServiceInterface.class);
    Call<SCommandResultOutput> cmdCall = service.createL3vpn(l3vpn);
    Response<SCommandResultOutput> response = cmdCall.execute();
    ServiceUtil.parseCommandResultOutput(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
  }

  /**
   * Send REST to controller to delete L3vpn..
   *
   * @param l3vpnId L3vpn UUID
   */
  public void deleteL3vpn(String l3vpnId)
      throws IOException, HttpErrorException, CommandErrorException {
    String printText = "Delete L3vpn " + l3vpnId;
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    L3ServiceInterface service = retrofit.create(L3ServiceInterface.class);
    Call<SCommandResultOutput> cmdCall = service.deleteL3vpn(l3vpnId);
    Response<SCommandResultOutput> response = cmdCall.execute();
    ServiceUtil.parseRpcResult(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
  }
}
