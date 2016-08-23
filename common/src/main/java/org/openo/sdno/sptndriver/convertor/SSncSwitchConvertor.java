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

import org.openo.sdno.sptndriver.enums.south.sncswitch.SLinearProtectionProtocal;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SLinearProtectionType;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SRevertiveMode;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SSncSwitchLayerRate;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SSwitchMode;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NPathProtectPolicy;
import org.openo.sdno.sptndriver.models.south.SSncSwitch;

public class SSncSwitchConvertor {

  private static final int default_wtr = 300;
  private static final int default_holdoff_time = 0;
  private static final int default_reroute_wtr = 50;
  private static final SSwitchMode defaultSwitchMode = SSwitchMode.double_end_switch;

  public static SSncSwitch initPwSncSwitch(String sncId,boolean hasProt) {
    SSncSwitch pwSncSwitch = new SSncSwitch();
    // 1. snc id,
    pwSncSwitch.setSncId(sncId);
    // 2. layer rate
    pwSncSwitch.setLayerRate(Integer.getInteger(SSncSwitchLayerRate.PW.toString()));
    // 3. linear protection type
    if (hasProt) {
      pwSncSwitch.setLinearProtectionType(
          Integer.getInteger(
              SLinearProtectionType.path_protection_1_to_1.toString()));
    } else {
      pwSncSwitch.setLinearProtectionType(Integer.getInteger(
          SLinearProtectionType.unprotected.toString()));
    }
    // 4. linear protection protocal
    pwSncSwitch.setLinearProtectionProtocol(Integer.getInteger(
        SLinearProtectionProtocal.APS.toString()));
    // 5. switch mode
    pwSncSwitch.setSwitchMode(Integer.getInteger(
        defaultSwitchMode.toString()));
    // 6. tevertive mode
    pwSncSwitch.setRevertiveMode(Integer.getInteger(SRevertiveMode.revertive.toString()));
    // 7. wait time
    pwSncSwitch.setWtr(Integer.valueOf(default_wtr));
    // 8. hode off time
    pwSncSwitch.setHoldOffTime(Integer.valueOf(default_holdoff_time));
    // 9. reroute revertive mode
    pwSncSwitch.setRerouteRevertiveMode(Integer.getInteger(SRevertiveMode.no_revertive.toString()));
    // 10. reroute wait time
    pwSncSwitch.setRerouteWtr(Integer.valueOf(default_reroute_wtr));
    return pwSncSwitch;
  }

  public static SSncSwitch initLspSncSwitch(NMplsTePolicy policy) {
    SSncSwitch sncSwitch = new SSncSwitch();
    boolean hasProt = false;
    boolean coRoute = false;
    SRevertiveMode revertiveMode = SRevertiveMode.revertive;
    int wtr = default_wtr;
    if (policy != null && policy.getCoRoute() != null) {
      coRoute = policy.getCoRoute().booleanValue();
    }

    if (policy != null && policy.getPathProtectPolicy() != null) {
      NPathProtectPolicy nPathProtectPolicy = policy.getPathProtectPolicy();
      hasProt = true;
      if (nPathProtectPolicy.getRetrieve() != null
          && nPathProtectPolicy.getRetrieve().booleanValue() == false) {
        revertiveMode = SRevertiveMode.no_revertive;
      }
      if (nPathProtectPolicy.getWtr() != null) {
        wtr = nPathProtectPolicy.getWtr().intValue() / 1000000;
      }
    }

    // 1. snc id, configured by controller since drive doesn't know UUID yet
    sncSwitch.setSncId(null);
    // 2. layer rate
    sncSwitch.setLayerRate(Integer.getInteger(SSncSwitchLayerRate.LSP.toString()));
    // 3. linear protection type
    sncSwitch
        .setLinearProtectionType(Integer.getInteger(getLspProtType(hasProt, coRoute).toString()));
    // 4. linear protection protocal
    sncSwitch.setLinearProtectionProtocol(Integer.getInteger(
        SLinearProtectionProtocal.APS.toString()));
    // 5. switch mode
    sncSwitch.setSwitchMode(Integer.getInteger(
        defaultSwitchMode.toString()));
    // 6. tevertive mode
    sncSwitch.setRevertiveMode(Integer.getInteger(revertiveMode.toString()));
    // 7. wait time
    sncSwitch.setWtr(Integer.valueOf(wtr));
    // 8. hode off time
    sncSwitch.setHoldOffTime(Integer.valueOf(default_holdoff_time));
    // 9. reroute revertive mode
    sncSwitch.setRerouteRevertiveMode(Integer.getInteger(SRevertiveMode.no_revertive.toString()));
    // 10. reroute wait time
    sncSwitch.setRerouteWtr(Integer.valueOf(default_reroute_wtr));

    return sncSwitch;
  }

  // todo need confirm
  private static SLinearProtectionType getLspProtType(boolean hasProt, boolean needReroute) {
    if (!hasProt && !needReroute) {
      return SLinearProtectionType.unprotected;
    }

    if (!hasProt && needReroute) {
      return SLinearProtectionType.unprotected_with_recovery;
    }

    if (hasProt && !needReroute) {
      return SLinearProtectionType.path_protection_1_to_1;
    }

    if (hasProt && needReroute) {
      return SLinearProtectionType.with_recovery_1_to_1;
    }

    return SLinearProtectionType.path_protection_1_to_1;
  }

}
