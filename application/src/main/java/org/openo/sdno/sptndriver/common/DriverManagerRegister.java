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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.parser.ParseException;
import org.openo.sdno.sptndriver.config.Config;
import org.openo.sdno.sptndriver.services.DriverManagerService;
import org.openo.sdno.sptndriver.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The class to register sptn driver to driver manager.
 */
public class DriverManagerRegister implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(DriverManagerRegister.class);

  private Object driverInfo;

  private Config config;

  public DriverManagerRegister(Config config) {
    this.config = config;
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

    DriverManagerService driverManagerService = new DriverManagerService(config.getMsbUrl());
    int retry = 1;
    while (!driverManagerService.registerDriver(driverInfo)) {
      LOGGER.warn(logText + " failed, sleep 30S and try again.");
      threadSleep(30000);
      LOGGER.info(logText + ": " + retry++);
    }
    LOGGER.info(logText + " success!");
    LOGGER.info(logText + " end.");
  }

  private void threadSleep(int second) {
    String logText = "Register sdn-o zte sptn driver to driver manager";
    LOGGER.info(logText + " start sleep ....");
    try {
      Thread.sleep(second);
    } catch (InterruptedException error) {
      LOGGER.error(ExceptionUtils.getStackTrace(error));
    }
    LOGGER.info(logText + " sleep end.");
  }

  private void initDriverInfo() {
    driverInfo = null;
    try {
      driverInfo = JsonUtil.readJsonFromFile("./conf/driver.json");
    } catch (IOException ex) {
      LOGGER.error(ExceptionUtils.getStackTrace(ex));
    } catch (ParseException ex) {
      LOGGER.error(ExceptionUtils.getStackTrace(ex));
    }
  }
}
