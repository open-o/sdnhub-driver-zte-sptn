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

package org.openo.sdno.sptndriver.simulator;

import org.openo.sdno.sptndriver.simulator.config.Config;
import org.openo.sdno.sptndriver.simulator.resouces.ElineResource;
import org.openo.sdno.sptndriver.simulator.resouces.L3Resource;
import org.openo.sdno.sptndriver.simulator.resouces.TunnelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 *  The application class of sptn controller simulator.
 */
public class App extends Application<Config> {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(App.class);

  /**
   * Main function.
   *
   * @param args args input by users
   */
  public static void main(String[] args) throws Exception {
    new App().run(args);
  }

  /**
   * Initialize before the service started.
   *
   * @param bootstrap Bootstrap.
   */
  @Override
  public void initialize(Bootstrap<Config> bootstrap) {
  }

  /**
   * Run application.
   *
   * @param config      configuration settings read from configuration file.
   * @param environment Environment.
   */
  @Override
  public void run(Config config, Environment environment) {
    LOGGER.info("Method App#run() called");

    // Add the resource to the environment
    environment.jersey().register(new L3Resource());
    environment.jersey().register(new ElineResource());
    environment.jersey().register(new TunnelResource());
  }
}
