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

package org.openo.sdno.sptndriver.services;

import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.models.south.SRouteCalResultsOutput;
import org.openo.sdno.sptndriver.utils.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * SBI Tunnel Services.
 */
public class STunnelServices {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(STunnelServices.class);
  private String baseUrl;

  /**
   * The Constructor.
   *
   * @param baseUrl url of SPTN controller.
   */
  public STunnelServices(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Calculate route of tunnels.
   *
   * @param routeCalcReqsInput route request.
   * @return route result.
   */
  public SRouteCalResultsOutput calcRoutes(SRouteCalReqsInput routeCalcReqsInput)
      throws HttpErrorException, IOException, CommandErrorException {
    String printText = "Calculate route of tunnel ";
    LOGGER.debug(printText + " begin. ");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    ISTunnelService service = retrofit.create(ISTunnelService.class);
    Call<SRouteCalResultsOutput> repos = service.calcRoutes(routeCalcReqsInput);
    Response<SRouteCalResultsOutput> response = repos.execute();
    SRouteCalResultsOutput output = ServiceUtil.parseResponse(response, LOGGER, printText);
    LOGGER.debug(printText + " end. ");
    return output;
  }
}
