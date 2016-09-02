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

package org.openo.sdno.sptndriver.enums.ac;

/**
 * Enum of access type.
 */
public enum AccessTypeEnum {
  PORT("port", 1),
  DOT1Q("dot1q", 2),
  QINQ("qinq", 3);
  private String name;
  private Integer index;

  private AccessTypeEnum(String name, Integer index) {
    this.name = name;
    this.index = index;
  }

  /**
   * Convert SBI integer access type to NBI string access type.
   *
   * @param index Integer value of NBI access type.
   * @return String value of SBI access type.
   */
  public static String getName(Integer index) {
    for (AccessTypeEnum e : AccessTypeEnum.values()) {
      if (e.getIndex().equals(index)) {
        return e.name;
      }
    }
    return null;
  }

  /**
   * Convert NBI string access type to NBI integer access type.
   *
   * @param name String value of SBI access type.
   * @return Integer value of NBI access type.
   */
  public static Integer getIndex(String name) {
    for (AccessTypeEnum e : AccessTypeEnum.values()) {
      if (e.getName().equals(name)) {
        return e.index;
      }
    }
    return null;
  }

  /**
   * Get NBI access type.
   */
  public String getName() {
    return name;
  }

  /**
   * Get SBI access type.
   */
  public Integer getIndex() {
    return index;
  }
}
