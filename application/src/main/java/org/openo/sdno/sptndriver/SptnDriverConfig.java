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


import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;
import org.openo.sdno.sptndriver.config.Config;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

/**
 * Configuration class of SDN-O SPTN driver.
 */
public class SptnDriverConfig extends Configuration implements Config {

  /**
   * Default time out of rest command, in seconds.
   */
  @JsonProperty
  private int timeout = 600;
  @JsonProperty
  @NotEmpty
  private String msbUrl;
  @JsonProperty
  private DataSourceFactory database = new DataSourceFactory();

  @Override
  public int getTimeout() {
    return timeout;
  }

  @Override
  public String getMsbUrl() {
    return msbUrl;
  }

  @Override
  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

}
