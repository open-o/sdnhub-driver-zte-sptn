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

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.models.south.SRouteCalResultsOutput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * SBI Tunnel service interface.
 */
public interface ISTunnelService {

  /**
   * Calculate tunnel routes.
   *
   * @param routeCalcReqsInput Request to calculate tunnel routes.
   * @return Tunnel routes result.
   */
  @Headers(Config.CONTROLLER_ICT_AUTH)
  @POST("restconf/operations/sptn-service-route:request-routes")
  Call<SRouteCalResultsOutput> calcRoutes(
      @Body SRouteCalReqsInput routeCalcReqsInput);
}
