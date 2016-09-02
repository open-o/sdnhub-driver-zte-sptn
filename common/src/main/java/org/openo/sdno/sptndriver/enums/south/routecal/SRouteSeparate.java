/*
 * Copyright 2016 ZTE, Inc. and others.
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

package org.openo.sdno.sptndriver.enums.south.routecal;

/**
 * Enum of the route separate policy when calculate work route and protection route.
 */
public enum SRouteSeparate {
  STRICT_SEPARATE(0),
  BEST_EFFORT_SEPARATE(1);
  private Integer value;

  SRouteSeparate(Integer value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

  /**
   * Get Integer route separate policy value.
   *
   * @return Integer route separate policy value.
   */
  public Integer getValue() {
    return value;
  }
}
