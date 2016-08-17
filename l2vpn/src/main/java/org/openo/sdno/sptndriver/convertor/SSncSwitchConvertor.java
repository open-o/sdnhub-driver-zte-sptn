package org.openo.sdno.sptndriver.convertor;

import org.openo.sdno.sptndriver.models.south.SSncSwitch;

/**
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
public class SSncSwitchConvertor {

  public static SSncSwitch initPwSncSwitch(String ElineUuid, boolean hasProt) {
    SSncSwitch pwSncSwitch = new SSncSwitch();
    // 1. snc id
    pwSncSwitch.setSncId(ElineUuid);
    // 2. layer rate
    pwSncSwitch.setLayerRate(Integer.valueOf(1));
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
        SSwitchMode.double_end_switch.toString()));
    // 6. tevertive mode
    pwSncSwitch.setRevertiveMode(Integer.getInteger(SRevertiveMode.revertive.toString()));
    // 7. wait time
    pwSncSwitch.setWtr(Integer.valueOf(300));
    // 8. hode off time
    pwSncSwitch.setHoldOffTime(Integer.valueOf(0));
    // 9. reroute revertive mode
    pwSncSwitch.setRerouteRevertiveMode(Integer.getInteger(SRevertiveMode.no_revertive.toString()));
    // 10. reroute wait time
    pwSncSwitch.setRerouteWtr(Integer.valueOf(50));
    return pwSncSwitch;
  }

  public static enum SLinearProtectionType {
    unprotected(0),
    path_protection_1_to_1(1),
    path_protection_1_plus_1(2),
    unprotected_with_recovery(3),
    with_recovery_1_to_1(4),
    with_recovery_1_plus_1(5),
    permanent_1_plus_1_protection(6);
    private Integer value;

    SLinearProtectionType(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }

  public static enum SLinearProtectionProtocal {
    APS(0),
    PSC(1);

    private Integer value;

    SLinearProtectionProtocal(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }

  public static enum SSwitchMode {
    single_ended_switch(0),
    double_end_switch(1);

    private Integer value;

    SSwitchMode(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }

  public static enum SRevertiveMode {
    no_revertive(0),
    revertive(1);

    private Integer value;

    SRevertiveMode(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }
}
