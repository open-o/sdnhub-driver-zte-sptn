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

public class AdminStatusConvertor {

  public final static String n_adminDown = "adminDown";
  public final static String n_adminUp = "adminUp";
  public final static Integer s_adminDown = Integer.valueOf(1);
  public final static Integer s_adminUp = Integer.valueOf(0);

  public static Integer NToS(String adminStatus) {
    if (adminStatus != null && adminStatus.equals(n_adminDown)) {
      return s_adminDown;
    } else {
      return s_adminUp;
    }
  }

  public static String SToN(Integer adminStatus) {
    if (adminStatus != null && adminStatus.equals(s_adminDown)) {
      return n_adminDown;
    } else {
      return n_adminUp;
    }
  }
}
