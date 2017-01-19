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

package org.openo.sdnhub.sptndriver.utils;

import org.openo.sdnhub.sptndriver.exception.ControllerNotFoundException;
import org.openo.sdnhub.sptndriver.models.esr.SdnController;
import org.openo.sdnhub.sptndriver.services.EsrService;

/**
 * The Utility class of External Service Register(ESR).
 */
public class EsrUtil {

    private EsrUtil(){}

    /**
     * Get SDN-O controller URL by controller id.
     *
     * @param controllerId SDN-O controller id.
     * @return SDN-O controller URL.
     */
    public static String getSdnoControllerUrl(String controllerId)
        throws ControllerNotFoundException {
        EsrService esrService = new EsrService();
        SdnController sdnController;
        try {
            sdnController = esrService.getSdnoController(controllerId);
        } catch (Exception ex) {
            throw new ControllerNotFoundException(ex, controllerId);
        }
        return sdnController.getUrl();
    }
}
