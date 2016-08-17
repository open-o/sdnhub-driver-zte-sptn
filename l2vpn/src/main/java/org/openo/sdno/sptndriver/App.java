package org.openo.sdno.sptndriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.openo.sdno.sptndriver.resources.L2Resource;

/**
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

public class App extends Application<Config> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(App.class);

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }

  @Override
  public void initialize(Bootstrap<Config> b) {
  }

  @Override
  public void run(Config c, Environment e) throws Exception {
    LOGGER.info("Method App#run() called");

    // Add the resource to the environment
    e.jersey().register(new L2Resource(e.getValidator()));


  }
}
