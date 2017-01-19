/*
 * Copyright 2016-2017 ZTE Corporation.
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

package org.openo.sdnhub.sptndriver.services;

import org.openo.sdnhub.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdnhub.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdnhub.sptndriver.models.south.SDeleteElineInput;
import org.openo.sdnhub.sptndriver.config.AppConfig;
import org.openo.sdnhub.sptndriver.models.south.SCommandResultOutput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 *  Interface of SBI E-Line service provided by SPTN controller.
 */
public interface ElineServiceInterface {

  /**
   *  Create E-Line and tunnels.
   * @param createElineAndTunnels  Input parameter of create E-Line and tunnels.
   * @return Command result, including success, fail and partially fail.
   */
  @Headers(AppConfig.CONTROLLER_ICT_AUTH)
  @POST("restconf/operations/sptn-service-eline:create-snc-eline-tunnels")
  Call<SCmdResultAndNcdResRelationsOutput> createElineAndTunnels(
      @Body SCreateElineAndTunnelsInput createElineAndTunnels);

  /**
   *  Delete E-Line.
   * @param elineId UUID of E-Line to be delete.
   * @return Command result, including success, fail and partially fail.
   */
  @Headers(AppConfig.CONTROLLER_ICT_AUTH)
  @POST("restconf/operations/sptn-service-eline:delete-snc-eline")
  Call<SCommandResultOutput> deleteEline(@Body SDeleteElineInput elineId);
}
