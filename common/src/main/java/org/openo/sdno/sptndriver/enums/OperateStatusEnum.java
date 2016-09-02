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

/**
 * Enum of operate status
 */
public enum OperateStatusEnum {
  UP("operateUp", 0),
  DOWN("operateDown", 1);
  private String name;
  private Integer index;

  private OperateStatusEnum(String name, Integer index) {
    this.name = name;
    this.index = index;
  }

  /**
   * Convert SBI integer operate status to NBI string operate status
   *
   * @param index Integer value of NBI operate status
   * @return String value of SBI operate status
   */
  public static String getName(Integer index) {
    for (OperateStatusEnum e : OperateStatusEnum.values()) {
      if (e.getIndex().equals(index)) {
        return e.name;
      }
    }
    return null;
  }

  /**
   * Convert NBI string operate status to NBI integer operate status
   *
   * @param name String value of SBI operate status
   * @return Integer value of NBI operate status
   */
  public static Integer getIndex(String name) {
    for (OperateStatusEnum e : OperateStatusEnum.values()) {
      if (e.getName().equals(name)) {
        return e.index;
      }
    }
    return null;
  }

  /**
   * Get NBI operate status
   */
  public String getName() {
    return name;
  }

  /**
   * Get SBI operate status
   */
  public Integer getIndex() {
    return index;
  }
}
