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
 * Enum of Route calculate policy in route constraint.
 */
public enum SRouteConstraintPolicy {
  MINIMUM_HOP(0),
  BANDWIDTH_BALANCING(1),
  MINIMUM_LATENCY(2);
  private Integer value;

  SRouteConstraintPolicy(Integer value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

  /**
   * Get Integer route calculate policy value in route constraint.
   *
   * @return Integer route calculate policy value in route constraint.
   */
  public Integer getValue() {
    return value;
  }
}
