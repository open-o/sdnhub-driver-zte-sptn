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


import org.openo.sdnhub.sptndriver.models.south.brs.SME;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface of SBI NE service.
 */
public interface ISNeService {

  /**
   * Get NE information by NE ID.
   *
   * @param neid NE UUID in controller.
   * @return NE information.
   */
  @GET("restconf/data/sptn-resources-module:resources/nes/ne-list/ne={id}")
  Call<SME> getNeByID(@Path("id") String neid);
}
