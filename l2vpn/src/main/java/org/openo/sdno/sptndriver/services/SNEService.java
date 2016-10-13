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


import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.brs.SME;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The class of NE service.
 */
public class SNEService {

  private String baseUrl;

  /**
   * The constructor.
   *
   * @param baseUrl Base URL of controller.
   */
  public SNEService(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Get NE by NE UUID in controller.
   *
   * @param neid NE UUID in controller.
   * @return NE information.
   */
  public SME getNeByID(String neid) throws HttpErrorException, IOException {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISNeService service = retrofit.create(ISNeService.class);
    Call<SME> repos = service.getNeByID(neid);

    Response<SME> response = repos.execute();
    if (response.isSuccessful()) {
      return response.body();
    } else {
      throw new HttpErrorException(response);
    }
  }

}
