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

package org.openo.sdno.sptndriver.enums.ac;

import org.openo.sdno.sptndriver.models.south.SServiceEndPoint;

/**
 * Enumerator of access type.
 */
public enum AccessTypeEnum {
  PORT("port", SServiceEndPoint.AccessTypeEnum.PORT),
  DOT1Q("dot1q", SServiceEndPoint.AccessTypeEnum.DOT1Q),
  QINQ("qinq", SServiceEndPoint.AccessTypeEnum.QINQ);
  private String north;
  private SServiceEndPoint.AccessTypeEnum south;

  AccessTypeEnum(String north, SServiceEndPoint.AccessTypeEnum south) {
    this.north = north;
    this.south = south;
  }

  /**
   * Convert SBI access type to NBI access type.
   *
   * @param south SBI access type.
   * @return NBI access type.
   */
  public static String convertSouthToNorth(SServiceEndPoint.AccessTypeEnum south) {
    for (AccessTypeEnum accessTypeEnum : AccessTypeEnum.values()) {
      if (accessTypeEnum.getSouth().equals(south)) {
        return accessTypeEnum.north;
      }
    }
    return null;
  }

  /**
   * Convert NBI access type to SBI access type.
   *
   * @param north NBI access type.
   * @return SBI access type.
   */
  public static SServiceEndPoint.AccessTypeEnum convertNbiToSbi(String north) {
    for (AccessTypeEnum e : AccessTypeEnum.values()) {
      if (e.getNorth().equals(north)) {
        return e.south;
      }
    }
    return null;
  }

  /**
   * Get NBI access type.
   */
  public String getNorth() {
    return north;
  }

  /**
   * Get SBI access type.
   */
  public SServiceEndPoint.AccessTypeEnum getSouth() {
    return south;
  }
}
