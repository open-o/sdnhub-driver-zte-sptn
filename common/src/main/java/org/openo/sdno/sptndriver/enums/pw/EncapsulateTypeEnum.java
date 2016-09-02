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

package org.openo.sdno.sptndriver.enums.pw;

/**
 * Enum of encapsulate type.
 */
public enum EncapsulateTypeEnum {
  VLAN("vlan", 4),
  ETHERNET("eth", 5);
  private String name;
  private Integer index;

  private EncapsulateTypeEnum(String name, Integer index) {
    this.name = name;
    this.index = index;
  }

  /**
   * Convert SBI integer encapsulate type to NBI string encapsulate type.
   *
   * @param index Integer value of NBI encapsulate type.
   * @return String value of SBI encapsulate type.
   */
  public static String getName(Integer index) {
    for (EncapsulateTypeEnum e : EncapsulateTypeEnum.values()) {
      if (e.getIndex().equals(index)) {
        return e.name;
      }
    }
    return null;
  }

  /**
   * Convert NBI string encapsulate type to NBI integer encapsulate type.
   *
   * @param name String value of SBI encapsulate type.
   * @return Integer value of NBI encapsulate type.
   */
  public static Integer getIndex(String name) {
    for (EncapsulateTypeEnum e : EncapsulateTypeEnum.values()) {
      if (e.getName().equals(name)) {
        return e.index;
      }
    }
    return null;
  }

  /**
   * Get NBI encapsulate type.
   */
  public String getName() {
    return name;
  }

  /**
   * Get SBI encapsulate type.
   */
  public Integer getIndex() {
    return index;
  }
}
