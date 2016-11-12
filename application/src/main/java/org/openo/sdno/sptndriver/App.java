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

package org.openo.sdno.sptndriver;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.eclipse.jetty.util.component.LifeCycle;
import org.openo.sdno.sptndriver.common.DriverManagerRegister;
import org.openo.sdno.sptndriver.common.DriverManagerUnregister;
import org.openo.sdno.sptndriver.config.AppConfig;
import org.openo.sdno.sptndriver.healthCheck.CustomHealthCheck;
import org.openo.sdno.sptndriver.resources.L2Resource;
import org.openo.sdno.sptndriver.resources.L3Resource;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;


/**
 * SDN-O SPTN driver application class.
 */
public class App extends Application<SptnDriverConfig> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(App.class);

  private Thread driverManagerRegister;

  public static String driverInstanceId;

  /**
   * Main function.
   *
   * @param args arguments, input by users
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
  public void initialize(Bootstrap<SptnDriverConfig> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/api-doc", "/api-doc", "index.html", "api-doc"));
  }

  /**
   * Run application.
   *
   * @param config      configuration settings read from configuration file.
   * @param environment Environment.
   */
  @Override
  public void run(final SptnDriverConfig config, Environment environment) {
    LOGGER.info("Method App#run() called");
    AppConfig.setConfig(config);
    // Create a DBI factory and build a JDBI instance
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "mysql");

    CustomHealthCheck healthCheck = new CustomHealthCheck();
    environment.healthChecks().register("healthcheck", healthCheck);

    // Add the resource to the environment
    environment.jersey().register(new L2Resource(environment.getValidator(), config, jdbi));
    environment.jersey().register(new L3Resource(environment.getValidator(), config, jdbi));

    initSwaggerConfig(config, environment);

    registerToDriverMgr(config);

    addLifeCycleListener(config, environment);
  }


  private void initSwaggerConfig(SptnDriverConfig configuration, Environment environment) {
    environment.jersey().register(new ApiListingResource());
    environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    BeanConfig config = new BeanConfig();
    config.setTitle("Open-o SDN-O ZTE SPTN Drvier API");
    config.setVersion("1.0.0");
    config.setResourcePackage("org.openo.sdno.sptndriver.resources");
    // set rest api basepath in swagger
    DefaultServerFactory serverFactory =
        (DefaultServerFactory) configuration.getServerFactory();
    String basePath = serverFactory.getApplicationContextPath();
    String rootPath = serverFactory.getJerseyRootPath();
    rootPath = rootPath.substring(0, rootPath.indexOf("/*"));
    basePath =
        basePath.equals("/") ? rootPath : (new StringBuilder()).append(basePath).append(rootPath)
            .toString();
    config.setBasePath(basePath);
    config.setScan(true);
  }

  private void registerToDriverMgr(SptnDriverConfig config) {
    driverManagerRegister = new Thread(new DriverManagerRegister(config));
    driverManagerRegister.setName("Register sdn-o sptn driver to Driver Manager");
    driverManagerRegister.start();
  }

  private void unregisterFromDriverMgr(SptnDriverConfig config) {
    Thread driverManagerUnregister = new Thread(new DriverManagerUnregister(config));
    driverManagerUnregister.setName("Unregister sdn-o sptn driver to Driver Manager");
    driverManagerUnregister.start();
  }

  private void addLifeCycleListener(final SptnDriverConfig config, Environment environment) {
    environment.lifecycle().addLifeCycleListener(new LifeCycle.Listener() {
      @Override
      public void lifeCycleStarting(LifeCycle lifeCycle) {

      }

      @Override
      public void lifeCycleStarted(LifeCycle lifeCycle) {

      }

      @Override
      public void lifeCycleFailure(LifeCycle lifeCycle, Throwable throwable) {
        driverManagerRegister.interrupt();
        unregisterFromDriverMgr(config);
      }

      @Override
      public void lifeCycleStopping(LifeCycle lifeCycle) {
        driverManagerRegister.interrupt();
        unregisterFromDriverMgr(config);
      }

      @Override
      public void lifeCycleStopped(LifeCycle lifeCycle) {

      }
    });
  }

}
