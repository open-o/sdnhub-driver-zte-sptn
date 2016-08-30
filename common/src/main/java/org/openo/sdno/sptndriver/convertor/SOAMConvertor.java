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

import org.openo.sdno.sptndriver.enums.south.oam.SDmMode;
import org.openo.sdno.sptndriver.enums.south.oam.SLmMode;
import org.openo.sdno.sptndriver.enums.south.qos.STrafficClass;
import org.openo.sdno.sptndriver.models.south.SMep;
import org.openo.sdno.sptndriver.models.south.SMeps;
import org.openo.sdno.sptndriver.models.south.SOam;

public class SOamConvertor {

  public static SOam initOAM(String sncId) {
    SOam oam = new SOam();
    oam.setBelongedId(sncId);
    oam.setName(null);
    oam.setMegId("-1");
    oam.setMeps(initMeps());
    oam.setMeps(null);
    oam.setCcAllow(false);
    oam.setCcExp(Integer.getInteger(STrafficClass.CS7.toString()));
    oam.setCcInterval(SOam.CcIntervalEnum.NUMBER_3_DOT_3);
    oam.setLmMode(Integer.getInteger(SLmMode.disable.toString()));
    oam.setDmMode(Integer.getInteger(SDmMode.disable.toString()));
    return oam;
  }

  private static SMeps initMeps() {
    SMep mep1 = new SMep();
    mep1.setId(1);
    SMep mep2 = new SMep();
    mep2.setId(2);
    SMeps meps = new SMeps();
    meps.add(mep1);
    meps.add(mep2);
    return meps;
  }
}
