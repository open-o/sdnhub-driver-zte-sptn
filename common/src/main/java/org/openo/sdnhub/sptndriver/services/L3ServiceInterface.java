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

import org.openo.sdnhub.sptndriver.config.AppConfig;
import org.openo.sdnhub.sptndriver.models.south.SCmdResultAndNcdResRelations;
import org.openo.sdnhub.sptndriver.models.south.SCommandResultOutput;
import org.openo.sdnhub.sptndriver.models.south.SL3vpnCreateInput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface of SBI L3vpn service provided by SPTN controller.
 */
public interface L3ServiceInterface {
    /**
     * Create L3vpn..
     *
     * @param l3vpn Input parameter of create L3vpn.
     * @return Command result, including success, fail and partially fail.
     */
    @Headers(AppConfig.CONTROLLER_ICT_AUTH)
    @POST("restconf/data/sptn-service-l3vpn:service/snc-l3vpns")
    Call<SCmdResultAndNcdResRelations> createL3vpn(@Body SL3vpnCreateInput l3vpn);

    /**
     * Delete L3vpn.
     *
     * @param l3vpnId UUID of L3vpn to be delete.
     * @return Command result, including success, fail and partially fail.
     */
    @Headers(AppConfig.CONTROLLER_ICT_AUTH)
    @DELETE("restconf/data/sptn-service-l3vpn:service/snc-l3vpns/snc-l3vpn={id}")
    Call<SCommandResultOutput> deleteL3vpn(@Path("id") String l3vpnId);
}
