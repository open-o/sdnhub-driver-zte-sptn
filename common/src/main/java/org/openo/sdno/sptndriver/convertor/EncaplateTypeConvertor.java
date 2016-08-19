/*
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.sdno.sptndriver.convertor;

public class EncaplateTypeConvertor {

  public static Integer NToS(String nType) {
    if (nType != null && nType.equals(NEncaplateType.eth.toString())) {
      return Integer.getInteger(SEncaplateType.ethernet.toString());
    } else {
      return Integer.getInteger(SEncaplateType.vlan.toString());
    }
  }

  public static String SToN(Integer sType) {
    if (sType != null && sType.equals(Integer.getInteger(SEncaplateType.ethernet.toString()))) {
      return NEncaplateType.eth.toString();
    } else {
      return NEncaplateType.vlan.toString();
    }
  }

  public enum SEncaplateType {
    vlan(4),
    ethernet(5);
    private Integer value;

    SEncaplateType(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }

  public enum NEncaplateType {
    vlan("eth"),
    eth("vlan");
    private String value;

    NEncaplateType(String value) {
      this.value = value;
    }

    public String toString() {
      return value;
    }
  }


}
