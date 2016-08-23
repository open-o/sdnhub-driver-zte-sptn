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

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.enums.south.SCmdResultStatus;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SCommandResult;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SElineServices {

  @Inject
  Config configuration;

  public void createElineAndTunnels(SCreateElineAndTunnels createElineAndTunnels)
      throws HttpErrorException, IOException, CommandErrorException {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(configuration.getControllerUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISElineService service = retrofit.create(ISElineService.class);
    Call<SCommandResult> repos = service.createElineAndTunnels(createElineAndTunnels);

    Response<SCommandResult> response = repos.execute();
    if (response.isSuccessful()) {
      SCommandResult commandResult = response.body();
      if (commandResult.getResult() != null &&
          commandResult.getResult()
              .equals(Integer.getInteger(SCmdResultStatus.success.toString()))) {
        return;
      } else {
        throw new CommandErrorException(commandResult);
      }
    } else {
      throw new HttpErrorException(response);
    }
  }

  public void deleteEline(String elineId) throws IOException, HttpErrorException {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(configuration.getControllerUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISElineService service = retrofit.create(ISElineService.class);
    Call<Response> repos = service.deleteEline(elineId);
    Response<Response> response = repos.execute();
    if (response.isSuccessful()) {
      return;
    } else {
      throw new HttpErrorException(response);
    }
  }
}
