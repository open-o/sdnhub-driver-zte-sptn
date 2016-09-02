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
 * Enum of access action.
 */
public enum AccessActionEnum {
  KEEP("keep", 1),
  PUSH("push", 2),
  POP("pop", 3),
  SWAP("swap", 4);
  private String name;
  private Integer index;

  private AccessActionEnum(String name, Integer index) {
    this.name = name;
    this.index = index;
  }

  /**
   * Convert SBI integer access action to NBI string access action.
   *
   * @param index Integer value of NBI access action.
   * @return String value of SBI access action.
   */
  public static String getName(Integer index) {
    for (AccessActionEnum e : AccessActionEnum.values()) {
      if (e.getIndex().equals(index)) {
        return e.name;
      }
    }
    return null;
  }

  /**
   * Convert NBI string access action to NBI integer access action.
   *
   * @param name String value of SBI access action.
   * @return Integer value of NBI access action.
   */
  public static Integer getIndex(String name) {
    for (AccessActionEnum e : AccessActionEnum.values()) {
      if (e.getName().equals(name)) {
        return e.index;
      }
    }
    return null;
  }

  /**
   * Get NBI access action.
   */
  public String getName() {
    return name;
  }

  /**
   * Get SBI access action.
   */
  public Integer getIndex() {
    return index;
  }
}
