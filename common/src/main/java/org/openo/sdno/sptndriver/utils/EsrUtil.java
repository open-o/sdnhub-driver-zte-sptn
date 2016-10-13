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

package org.openo.sdno.sptndriver.utils;

import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.exception.CommandErrorException;
import org.openo.sdno.sptndriver.exception.HttpErrorException;
import org.openo.sdno.sptndriver.models.esr.SdnController;
import org.openo.sdno.sptndriver.services.EsrService;

import java.io.IOException;

/**
 *  The Utility class of External Service Register(ESR).
 */
public class EsrUtil {

  /**
   * Get SDN-O controller URL by controller id.
   * @param controllerId SDN-O controller id.
   * @param config Configuration.
   * @return SDN-O controller URL.
   */
  public static String getSdnoControllerUrl(String controllerId,
                                        Config config)
      throws IOException, HttpErrorException, CommandErrorException {
    EsrService esrService = new EsrService(config.getMsbUrl());
    SdnController sdnController = esrService.getSdnoController(controllerId);
    return sdnController.getUrl();
  }
}
