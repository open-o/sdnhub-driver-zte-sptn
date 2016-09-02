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

package org.openo.sdno.sptndriver.converter;

import org.openo.sdno.sptndriver.enums.south.sncswitch.SLinearProtectionProtocol;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SLinearProtectionType;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SRevertiveMode;
import org.openo.sdno.sptndriver.enums.south.SSncLayerRate;
import org.openo.sdno.sptndriver.enums.south.sncswitch.SSwitchMode;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NPathProtectPolicy;
import org.openo.sdno.sptndriver.models.south.SSncSwitch;

/**
 * The class to initiate snc switch.
 */
public class SSncSwitchInitiator {

  private static final int DEFAULT_WTR = 300;
  private static final int DEFAULT_HOLDOFF_TIME = 0;
  private static final int DEFAULT_REROUTE_WTR = 50;
  private static final SSwitchMode DEFAULT_SWITCH_MODE = SSwitchMode.DOUBLE_END_SWITCH;
  /**
   * NBI wtr in us, SBI wtr in seconds.
   */
  private static final int WTR_MULTIPLIER = 1000000;

  /**
   * Init PW snc switch.
   *
   * @param sncId        Service(Eline) UUID, not PW UUID.
   * @param hasSncSwitch Whether the service has PW snc switch.
   * @return PW snc switch.
   */
  public static SSncSwitch initPwSncSwitch(String sncId, boolean hasSncSwitch) {
    SSncSwitch pwSncSwitch = new SSncSwitch();

    pwSncSwitch.setSncId(sncId);
    pwSncSwitch.setLayerRate(SSncLayerRate.PW.getValue());

    // todo return null or the protection type is UNPROTECTED, need test
    if (hasSncSwitch) {
      pwSncSwitch.setLinearProtectionType(
          Integer.getInteger(
              SLinearProtectionType.PATH_PROTECTION_1_TO_1.toString()));
    } else {
      pwSncSwitch.setLinearProtectionType(Integer.getInteger(
          SLinearProtectionType.UNPROTECTED.toString()));
    }

    pwSncSwitch.setLinearProtectionProtocol(Integer.getInteger(
        SLinearProtectionProtocol.APS.toString()));
    pwSncSwitch.setSwitchMode(Integer.getInteger(
        DEFAULT_SWITCH_MODE.toString()));
    pwSncSwitch.setRevertiveMode(Integer.getInteger(SRevertiveMode.REVERTIVE.toString()));
    pwSncSwitch.setWtr(Integer.valueOf(DEFAULT_WTR));
    pwSncSwitch.setHoldOffTime(Integer.valueOf(DEFAULT_HOLDOFF_TIME));
    pwSncSwitch.setRerouteRevertiveMode(Integer.getInteger(SRevertiveMode.NO_REVERTIVE.toString()));
    pwSncSwitch.setRerouteWtr(Integer.valueOf(DEFAULT_REROUTE_WTR));

    return pwSncSwitch;
  }

  /**
   * Init LSP snc switch.
   *
   * @param policy NBI MPLS TE policy.
   * @return LSP snc switch.
   */
  public static SSncSwitch initLspSncSwitch(NMplsTePolicy policy) {
    SSncSwitch sncSwitch = new SSncSwitch();
    boolean hasProt = false;
    boolean coRoute = false;
    SRevertiveMode revertiveMode = SRevertiveMode.REVERTIVE;
    int wtr = DEFAULT_WTR;
    if (policy != null && policy.getCoRoute() != null) {
      coRoute = policy.getCoRoute().booleanValue();
    }

    if (policy != null && policy.getPathProtectPolicy() != null) {
      NPathProtectPolicy nPathProtectPolicy = policy.getPathProtectPolicy();
      hasProt = true;
      if (nPathProtectPolicy.getRetrieve() != null
          && nPathProtectPolicy.getRetrieve().booleanValue() == false) {
        revertiveMode = SRevertiveMode.NO_REVERTIVE;
      }
      if (nPathProtectPolicy.getWtr() != null) {
        wtr = nPathProtectPolicy.getWtr().intValue() / WTR_MULTIPLIER;
      }
    }

    // snc id, configured by controller since drive doesn't know UUID yet
    sncSwitch.setSncId(null);
    sncSwitch.setLayerRate(SSncLayerRate.LSP.getValue());
    sncSwitch
        .setLinearProtectionType(Integer.getInteger(getLspProtType(hasProt, coRoute).toString()));
    sncSwitch.setLinearProtectionProtocol(Integer.getInteger(
        SLinearProtectionProtocol.APS.toString()));
    sncSwitch.setSwitchMode(Integer.getInteger(
        DEFAULT_SWITCH_MODE.toString()));
    sncSwitch.setRevertiveMode(Integer.getInteger(revertiveMode.toString()));
    sncSwitch.setWtr(Integer.valueOf(wtr));
    sncSwitch.setHoldOffTime(Integer.valueOf(DEFAULT_HOLDOFF_TIME));
    sncSwitch.setRerouteRevertiveMode(Integer.getInteger(SRevertiveMode.NO_REVERTIVE.toString()));
    sncSwitch.setRerouteWtr(Integer.valueOf(DEFAULT_REROUTE_WTR));

    return sncSwitch;
  }

  /**
   * Init protection type.
   *
   * @param hasProt     Whether LSP has protection.
   * @param needReroute Whether LSP need reroute when it is down.
   * @return LSP protection type.
   */
  private static SLinearProtectionType getLspProtType(boolean hasProt, boolean needReroute) {
    if (!hasProt && !needReroute) {
      return SLinearProtectionType.UNPROTECTED;
    }

    if (!hasProt && needReroute) {
      return SLinearProtectionType.UNPROTECTED_WITH_RECOVERY;
    }

    if (hasProt && !needReroute) {
      return SLinearProtectionType.PATH_PROTECTION_1_TO_1;
    }

    if (hasProt && needReroute) {
      return SLinearProtectionType.WITH_RECOVERY_1_TO_1;
    }

    return SLinearProtectionType.PATH_PROTECTION_1_TO_1;
  }

}
