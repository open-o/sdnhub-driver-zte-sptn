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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openo.sdno.sptndriver.App;
import org.openo.sdno.sptndriver.services.DriverManagerService;
import org.openo.sdno.sptndriver.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class to register sptn driver to driver manager.
 */
public class DriverManagerRegister implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerRegister.class);

    private Object driverInfo;


    public DriverManagerRegister() {
        initDriverInfo();
    }

    @Override
    public void run() {
        String logText = "Register sdn-o zte sptn driver to driver manager";
        LOGGER.info(logText + " begin");
        if (driverInfo == null) {
            LOGGER.error(logText + ": Read driver info from JSON file failed.");
            return;
        }

        DriverManagerService driverManagerService = new DriverManagerService();
        int retry = 1;
        while (!driverManagerService.registerDriver(driverInfo)) {
            LOGGER.warn(logText + " failed, sleep 30S and try again.");
            try {
                threadSleep(30000);
            } catch (InterruptedException ex) {
                LOGGER.error(logText + "is interrupted.");
                ExceptionUtils.getStackTrace(ex);
                Thread.currentThread().interrupt();
            }
            LOGGER.info(logText + ": " + retry++);
        }
        App.setDriverInstanceId(parseDriverInstanceId(driverInfo));
        LOGGER.info(logText + " success!");
        LOGGER.info(logText + " end.");
    }

    private void threadSleep(int second) throws InterruptedException {
        String logText = "Register sdn-o zte sptn driver to driver manager";
        LOGGER.info(logText + " start sleep ....");
        Thread.sleep(second);
        LOGGER.info(logText + " sleep end.");
    }

    private void initDriverInfo() {
        driverInfo = null;
        try {
            driverInfo = JsonUtil.readJsonFromFile("./conf/driver.json");
        } catch (Exception ex) {
            LOGGER.error("Failed to read ./conf/driver.json due to "
                + ExceptionUtils.getStackTrace(ex));
        }
    }

    private static String parseDriverInstanceId(Object driverInfo) {
        if (driverInfo == null) {
            return null;
        }
        Gson gson = new Gson();
        String str = gson.toJson(driverInfo);
        JsonObject jsonObject = gson.fromJson(str, JsonObject.class);
        String driverId = jsonObject.get("driverInfo").getAsJsonObject().get("instanceID").toString();
        String[] strArray = driverId.split("\"");
        return strArray[1];
    }

}
