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

package org.openo.sdno.sptndriver.convertor.ac;

import org.openo.sdno.sptndriver.enums.north.ac.NACAccessAction;
import org.openo.sdno.sptndriver.enums.south.ac.SACAccessAction;
import org.openo.sdno.sptndriver.enums.south.ac.SACAccessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccessActionConvertor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AccessActionConvertor.class);

  public static Integer NToS(String nType) {
    if (nType != null) {
      if (nType.equals(NACAccessAction.keep.toString())) {
        return Integer.getInteger(SACAccessAction.Keep.toString());
      } else if (nType.equals(NACAccessAction.pop.toString())) {
        return Integer.getInteger(SACAccessAction.Pop.toString());
      } else if (nType.equals(NACAccessAction.push.toString())) {
        return Integer.getInteger(SACAccessAction.Push.toString());
      } else if (nType.equals(NACAccessAction.swap.toString())) {
        return Integer.getInteger(SACAccessAction.Swap.toString());
      } else {
        LOGGER.error("unsupported ac access action enum type " + nType);
      }
    } else {
      LOGGER.error("ac access type is null.");
    }
    return Integer.getInteger(SACAccessType.port.toString());
  }
}
