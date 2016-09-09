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

import org.openo.sdno.sptndriver.enums.south.qos.SCacMode;
import org.openo.sdno.sptndriver.enums.south.qos.SColorMode;
import org.openo.sdno.sptndriver.enums.south.qos.SConvergeMode;
import org.openo.sdno.sptndriver.enums.south.qos.SQosPolicing;
import org.openo.sdno.sptndriver.enums.south.qos.STrafficAdjustMode;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NQosIfCar;
import org.openo.sdno.sptndriver.models.south.SQos;

/**
 * The class to initiate Qos parameters.
 */
public class SQosInitiator {

  /**
   * NBI cbs in bytes and SBI cbs in KBytes.
   */
  private static final int CBS_MULTIPLIER = 1000;
  /**
   * NBI pbs in bytes and SBI pbs in KBytes.
   */
  private static final int PBS_MULTIPLIER = 1000;

  /**
   * Init LSP Qos parameters according to MplsTePolicy.
   *
   * @param policy MplsTePolicy in NBI.
   * @return LSP Qos in SBI.
   */
  public static SQos initQos(NMplsTePolicy policy) {
    SQos qos = new SQos();
    qos.setBelongedId(null);
    qos.setTunnelMode(SQos.TunnelModeEnum.PIPELINE);

    qos.setConvgMode(SConvergeMode.NOT_CONVERGE.toString());
    // todo Whether support auto_adjust, equals to bandwidthMode in mplsTePolicy?
    qos.setTrafficAdjMode(STrafficAdjustMode.NOT_ADJUST.toString());
    // todo not sure how to config pir, cbs and pbs, need test
    if (policy != null && policy.getBandwidth() != null) {
      qos.setCacMode(SCacMode.OPEN.toString());
      qos.setA2zPolicing(SQosPolicing.OPEN.toString());
      qos.setZ2aPolicing(SQosPolicing.OPEN.toString());
      qos.setA2zCir(policy.getBandwidth().toString());
      qos.setZ2aCir(policy.getBandwidth().toString());
    } else {
      qos.setCacMode(SCacMode.CLOSE.toString());
      qos.setA2zPolicing(SQosPolicing.CLOSE.toString());
      qos.setZ2aPolicing(SQosPolicing.CLOSE.toString());
    }
    qos.setA2zColorMode(SColorMode.UNWARE.toString());
    qos.setZ2aColorMode(SColorMode.UNWARE.toString());
    qos.setTrafficClass(org.openo.sdno.sptndriver.models.south.STrafficClass.CS7);
    return qos;
  }

  /**
   * Init Qos in which cac mode and policing is closed.
   *
   * @param belongedId The UUID of object(PW or AC) which the Qos is belonged to.
   * @return Qos.
   */
  public static SQos initCacClosedQos(String belongedId) {
    SQos qos = new SQos();
    qos.setBelongedId(belongedId);
    qos.setTunnelMode(SQos.TunnelModeEnum.PIPELINE);
    qos.setConvgMode(null);
    qos.setTrafficAdjMode(null);
    qos.setCacMode(SCacMode.CLOSE.toString());
    qos.setA2zPolicing(SQosPolicing.CLOSE.toString());
    qos.setZ2aPolicing(SQosPolicing.CLOSE.toString());
    // todo check if cir need to be configured 0 or -1
    qos.setTrafficClass(org.openo.sdno.sptndriver.models.south.STrafficClass.CS7);
    return qos;
  }

  /**
   * Init AC Qos.
   *
   * @param AcId          AC UUID
   * @param upStreamQos   Upstream AC Qos.
   * @param downStreamQos Downstream AC Qos.
   * @return AC Qos.
   */
  public static SQos initAcQos(String AcId, NQosIfCar upStreamQos, NQosIfCar downStreamQos) {
    SQos qos = initCacClosedQos(AcId);
    if (upStreamQos != null && upStreamQos.getEnable()) {
      qos.setCacMode(SCacMode.OPEN.toString());
      qos.setA2zPolicing(SQosPolicing.OPEN.toString());
      qos.setA2zCir(upStreamQos.getCir().toString());
      qos.setA2zPir(upStreamQos.getPir().toString());
      qos.setA2zCbs(Integer.toString(upStreamQos.getCbs() / CBS_MULTIPLIER));
      qos.setA2zPbs(Integer.toString(upStreamQos.getPbs() / PBS_MULTIPLIER));
    }

    if (downStreamQos != null && downStreamQos.getEnable()) {
      qos.setCacMode(SCacMode.OPEN.toString());
      qos.setZ2aPolicing(SQosPolicing.OPEN.toString());
      qos.setZ2aCir(downStreamQos.getCir().toString());
      qos.setZ2aPir(downStreamQos.getPir().toString());
      qos.setZ2aCbs(Integer.toString(downStreamQos.getCbs() / CBS_MULTIPLIER));
      qos.setZ2aPbs(Integer.toString(downStreamQos.getPbs() / PBS_MULTIPLIER));
    }

    return qos;
  }
}
