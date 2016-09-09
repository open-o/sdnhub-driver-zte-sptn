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

package org.openo.sdno.sptndriver.enums;

import org.openo.sdno.sptndriver.models.south.SAdminStatus;

/**
 * Enum of admin status.
 */
public enum AdminStatusEnum {
  UP("adminUp", SAdminStatus.UP),
  DOWN("adminDown", SAdminStatus.DOWN);
  private String north;
  private SAdminStatus south;

  AdminStatusEnum(String north, SAdminStatus south) {
    this.north = north;
    this.south = south;
  }

  /**
   * Convert SBI integer admin status to NBI string admin status.
   *
   * @param southValue SBI admin status
   * @return NBI admin status
   */
  public static String convertSbiToNbi(SAdminStatus southValue) {
    for (AdminStatusEnum adminStatusEnum : AdminStatusEnum.values()) {
      if (adminStatusEnum.getSouthValue().equals(southValue)) {
        return adminStatusEnum.north;
      }
    }
    return null;
  }

  /**
   * Convert NBI admin status to SBI admin status.
   *
   * @param north NBI admin status
   * @return SBI admin status
   */
  public static SAdminStatus convertNbiToSbi(String north) {
    for (AdminStatusEnum adminStatusEnum : AdminStatusEnum.values()) {
      if (adminStatusEnum.getNorthValue().equals(north)) {
        return adminStatusEnum.south;
      }
    }
    return null;
  }

  /**
   * Get NBI admin status.
   */
  public String getNorthValue() {
    return north;
  }

  /**
   * Get SBI admin status.
   */
  public SAdminStatus getSouthValue() {
    return south;
  }
}
