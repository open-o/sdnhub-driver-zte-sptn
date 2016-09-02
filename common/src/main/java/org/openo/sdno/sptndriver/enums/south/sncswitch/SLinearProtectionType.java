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

import java.math.BigInteger;

/**
 *  Enum of linear protection type in snc switch..
 */
public enum SLinearProtectionType {
  UNPROTECTED(0),
  PATH_PROTECTION_1_TO_1(1),
  PATH_PROTECTION_1_PLUS_1(2),
  UNPROTECTED_WITH_RECOVERY(3),
  WITH_RECOVERY_1_TO_1(4),
  WITH_RECOVERY_1_PLUS_1(5),
  PERMANENT_1_PLUS_1_PROTECTION(6);
  private Integer value;

  SLinearProtectionType(Integer value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }

  /**
   *  Where the protection type is a protected type;
   * @param type  Protection type.
   * @return true if protection type is not equal to UNPROTECTED or UNPROTECTED_WITH_RECOVERY.
   */
  public static boolean isProtected(Integer type) {
    if (type.equals(UNPROTECTED.value)
        || type.equals(UNPROTECTED_WITH_RECOVERY.value)) {
      return false;
    }
    return true;
  }
}
