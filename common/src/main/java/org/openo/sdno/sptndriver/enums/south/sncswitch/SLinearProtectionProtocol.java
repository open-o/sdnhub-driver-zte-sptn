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

package org.openo.sdno.sptndriver.enums.south.sncswitch;

/**
 *  Enum of linear protection protocol in snc switch.
 */
public enum SLinearProtectionProtocol {
  APS(0),
  PSC(1);
  private Integer value;

  SLinearProtectionProtocol(Integer value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }
}
