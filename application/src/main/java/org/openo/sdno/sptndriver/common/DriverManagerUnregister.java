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

package org.openo.sdno.sptndriver.common;

import org.openo.sdno.sptndriver.App;
import org.openo.sdno.sptndriver.services.DriverManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class to unregister sptn driver from driver manager.
 */
public class DriverManagerUnregister implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerUnregister.class);

    @Override
    public void run() {
        LOGGER.info("Unregister sdn-o zte sptn driver from driver manager begin");
        if (App.getDriverInstanceId() == null) {
            LOGGER.error("Unregister sdn-o zte sptn driver from driver manager: "
                + "driverInstanceId is null.");
            return;
        }

        DriverManagerService driverManagerService = new DriverManagerService();

        if (!driverManagerService.unregisterDriver(App.getDriverInstanceId())) {
            LOGGER.error("Unregister sdn-o zte sptn driver from driver manager failed, id: "
                + App.getDriverInstanceId());
        } else {
            LOGGER.info("Unregister sdn-o zte sptn driver from driver manager success!");
        }
    }
}
