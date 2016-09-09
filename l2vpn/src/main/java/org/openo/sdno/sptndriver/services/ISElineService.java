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

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdno.sptndriver.models.south.SCommandResultOutput;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SDeleteElineInput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 *  Interface of SBI Eline service provided by SPTN controller.
 */
public interface ISElineService {

  /**
   *  Create Eline and tunnels.
   * @param createElineAndTunnels  Input parameter of create Eline and tunnels.
   * @return Command result, including success, fail and partially fail.
   */
  @Headers(Config.CONTROLLER_ICT_AUTH)
  @POST("restconf/operations/sptn-service-eline:create-snc-eline-tunnels")
  Call<SCmdResultAndNcdResRelationsOutput> createElineAndTunnels(
      @Body SCreateElineAndTunnelsInput createElineAndTunnels);

  /**
   *  Delete Eline.
   * @param elineId UUID of Eline to be delete.
   * @return Command result, including success, fail and partially fail.
   */
  @Headers(Config.CONTROLLER_ICT_AUTH)
  @POST("restconf/operations/sptn-service-eline:delete-snc-eline")
  Call<SCommandResultOutput> deleteEline(@Body SDeleteElineInput elineId);
}
