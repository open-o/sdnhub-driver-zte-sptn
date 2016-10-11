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

package org.openo.sdno.sptndriver.enums.l3vpn;

import org.openo.sdno.sptndriver.models.south.SL3acProtocol;

/**
 * Enumerator of route protocol.
 */
public enum RouteProtocolEnum {
  STATIC("static", SL3acProtocol.ProtocolTypeEnum.STATIC),
  OSPF("ospf", SL3acProtocol.ProtocolTypeEnum.OSPF),
  ISIS("isis",SL3acProtocol.ProtocolTypeEnum.ISIS),
  BGP("bgp",SL3acProtocol.ProtocolTypeEnum.BGP);
  private String north;
  private SL3acProtocol.ProtocolTypeEnum south;

  RouteProtocolEnum(String north, SL3acProtocol.ProtocolTypeEnum south) {
    this.north = north;
    this.south = south;
  }

  /**
   * Convert SBI route protocol to NBI route protocol.
   *
   * @param southValue SBI route protocol.
   * @return NBI route protocol.
   */
  public static String convertSbiToNbi(SL3acProtocol.ProtocolTypeEnum southValue) {
    for (RouteProtocolEnum adminStatusEnum : RouteProtocolEnum.values()) {
      if (adminStatusEnum.getSouthValue().equals(southValue)) {
        return adminStatusEnum.north;
      }
    }
    return null;
  }

  /**
   * Convert NBI route protocol to SBI route protocol.
   *
   * @param north NBI route protocol.
   * @return SBI route protocol.
   */
  public static SL3acProtocol.ProtocolTypeEnum convertNbiToSbi(String north) {
    for (RouteProtocolEnum adminStatusEnum : RouteProtocolEnum.values()) {
      if (adminStatusEnum.getNorthValue().equals(north)) {
        return adminStatusEnum.south;
      }
    }
    return null;
  }

  /**
   * Get NBI route protocol.
   */
  public String getNorthValue() {
    return north;
  }

  /**
   * Get SBI route protocol.
   */
  public SL3acProtocol.ProtocolTypeEnum getSouthValue() {
    return south;
  }
}
