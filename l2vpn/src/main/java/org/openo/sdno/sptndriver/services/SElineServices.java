/*
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.sdno.sptndriver.services;

import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Eline service CRUD
 */
public class SElineServices {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SElineServices.class);
  private String baseUrl;

  /**
   * constructor
   * @param baseUrl  url of SPTN controller
   */
  public SElineServices(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void createElineAndTunnels(SCreateElineAndTunnels createElineAndTunnels)
      throws HttpErrorException, IOException, CommandErrorException {
    String printText = "Create eline and tunnels " + createElineAndTunnels.getSncEline().getId();
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISElineService service = retrofit.create(ISElineService.class);
    Call<SCommandResultOutput> repos = service.createElineAndTunnels(createElineAndTunnels);
    Response<SCommandResultOutput> response = repos.execute();
    ServiceUtil.parseCommandResultOutput(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
  }

  public void deleteEline(SDeleteElineInput elineId)
      throws IOException, HttpErrorException, CommandErrorException {
    String printText = "Delete Eline " + elineId.getInput().getElineId();
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISElineService service = retrofit.create(ISElineService.class);
    Call<SCommandResultOutput> repos = service.deleteEline(elineId);
    Response<SCommandResultOutput> response = repos.execute();
    ServiceUtil.parseRPCResult(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
  }

}
