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

import org.openo.sdno.sptndriver.models.esr.SdnController;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface of SDN-O Controller ESR service
 */
public interface EsrServiceInterface {

    /**
     * Get a SDN-O controller by controller id.
     *
     * @param controllerId SDN-O controller id.
     * @return SDN-O controller information.
     */
    @GET("/openoapi/extsys/v1/sdncontrollers/{controllerId}")
    Call<SdnController> getSdnoController(@Path("controllerId") String controllerId);
}
