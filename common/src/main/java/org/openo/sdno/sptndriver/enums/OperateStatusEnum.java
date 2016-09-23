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

package org.openo.sdno.sptndriver.enums;

import org.openo.sdno.sptndriver.models.south.SOperateStatus;

/**
 * Enum of operate status.
 */
public enum OperateStatusEnum {
  UP("operateUp", SOperateStatus.UP),
  DOWN("operateDown", SOperateStatus.DOWN);
  private String northValue;
  private SOperateStatus southValue;

  OperateStatusEnum(String northValue, SOperateStatus southValue) {
    this.northValue = northValue;
    this.southValue = southValue;
  }

  /**
   * Convert SBI operate status to NBI operate status.
   *
   * @param southValue SBI operate status
   * @return NBI operate status
   */
  public static String convertSbiToNbi(SOperateStatus southValue) {
    for (OperateStatusEnum e : OperateStatusEnum.values()) {
      if (e.getSouthValue().equals(southValue)) {
        return e.northValue;
      }
    }
    return null;
  }

  /**
   * Convert NBI operate status to SBI operate status.
   *
   * @param northValue NBI operate status
   * @return SBI operate status
   */
  public static SOperateStatus convertNbiToSbi(String northValue) {
    for (OperateStatusEnum e : OperateStatusEnum.values()) {
      if (e.getNorthValue().equals(northValue)) {
        return e.southValue;
      }
    }
    return null;
  }

  /**
   * Get NBI operate status.
   */
  public String getNorthValue() {
    return northValue;
  }

  /**
   * Get SBI operate status.
   */
  public SOperateStatus getSouthValue() {
    return southValue;
  }
}
