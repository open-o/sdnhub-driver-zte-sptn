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

package org.openo.sdno.sptndriver.converter;

import org.openo.sdno.sptndriver.models.south.STrafficClass;

/**
 * The class to convert traffic class enum.
 */
public class TrafficClassConverter {

  /**
   * Convert string to SBI traffic class enum.
   *
   * @param trafficClass String traffic class.
   * @return Enum traffic class, return null if input is illegal.
   */
  public static STrafficClass getEnum(String trafficClass) {
    if (trafficClass == null) {
      return null;
    }
    for (STrafficClass enumTrafficClass : STrafficClass.values()) {
      if (enumTrafficClass.toString().equals(trafficClass)) {
        return enumTrafficClass;
      }
    }
    return null;
  }

}