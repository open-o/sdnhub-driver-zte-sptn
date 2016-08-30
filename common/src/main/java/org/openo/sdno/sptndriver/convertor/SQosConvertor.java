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

import org.openo.sdno.sptndriver.enums.south.qos.SCacMode;
import org.openo.sdno.sptndriver.enums.south.qos.SColorMode;
import org.openo.sdno.sptndriver.enums.south.qos.SConvergeMode;
import org.openo.sdno.sptndriver.enums.south.qos.SQosPolicing;
import org.openo.sdno.sptndriver.enums.south.qos.STrafficAdjustMode;
import org.openo.sdno.sptndriver.enums.south.qos.STrafficClass;
import org.openo.sdno.sptndriver.enums.south.qos.STunnelMode;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NQosIfCar;
import org.openo.sdno.sptndriver.models.south.SQos;

public class SQosConvertor {

  /**
   *  NBI cbs in bytes and SBI cbs in KBytes
   */
  private final static int CBS_MULTIPLIER = 1000;
  /**
   *  NBI pbs in bytes and SBI pbs in KBytes
   */
  private final static int PBS_MULTIPLIER = 1000;

  public static SQos initQos(NMplsTePolicy policy) {
    SQos qos = new SQos();
    // tunnel is created by controller, so the id is not known yet, controller should fill this field
    qos.setBelongedId(null);
    qos.setTunnelMode(Integer.getInteger(STunnelMode.pipeline.toString()));

    qos.setConvgMode(Integer.getInteger(SConvergeMode.not_converge.toString()));
    // todo Whether support auto_adjust, equals to bandwidthMode in mplsTePolicy?
    qos.setTrafficAdjMode(Integer.getInteger(STrafficAdjustMode.not_adjust.toString()));
    // todo not sure how to config pir, cbs and pbs, need test
    if (policy != null && policy.getBandwidth() != null) {
      qos.setCacMode(Integer.getInteger(SCacMode.open.toString()));
      qos.setA2zPolicing(Integer.getInteger(SQosPolicing.open.toString()));
      qos.setZ2aPolicing(Integer.getInteger(SQosPolicing.open.toString()));
      qos.setA2zCir(policy.getBandwidth().intValue());
      qos.setZ2aCir(policy.getBandwidth().intValue());
    } else {
      qos.setCacMode(Integer.getInteger(SCacMode.close.toString()));
      qos.setA2zPolicing(Integer.getInteger(SQosPolicing.close.toString()));
      qos.setZ2aPolicing(Integer.getInteger(SQosPolicing.close.toString()));
    }
    qos.setA2zColorMode(Integer.getInteger(SColorMode.unware.toString()));
    qos.setZ2aColorMode(Integer.getInteger(SColorMode.unware.toString()));
    qos.setTrafficClass(Integer.getInteger(STrafficClass.BE.toString()));
    return qos;
  }

  public static SQos initCacClosedQos(String pwId) {
    SQos qos = new SQos();
    qos.setBelongedId(pwId);
    qos.setTunnelMode(Integer.getInteger(STunnelMode.pipeline.toString()));
    qos.setConvgMode(null);
    qos.setTrafficAdjMode(null);
    qos.setCacMode(Integer.getInteger(SCacMode.close.toString()));
    qos.setA2zPolicing(Integer.getInteger(SQosPolicing.close.toString()));
    qos.setZ2aPolicing(Integer.getInteger(SQosPolicing.close.toString()));
    // todo check if cir need to be configured 0 or -1
    qos.setTrafficClass(Integer.getInteger(STrafficClass.CS7.toString()));
    return qos;
  }

  public static SQos initAcQos(String AcId, NQosIfCar upStreamQos, NQosIfCar downStreamQos) {
    SQos qos = initCacClosedQos(AcId);
    if (upStreamQos!= null && upStreamQos.getEnable()) {
      qos.setCacMode(Integer.getInteger(SCacMode.open.toString()));
      qos.setA2zPolicing(Integer.getInteger(SQosPolicing.open.toString()));
      qos.setA2zCir(upStreamQos.getCir());
      qos.setA2zPir(upStreamQos.getPir());
      qos.setA2zCbs(upStreamQos.getCbs()/CBS_MULTIPLIER);
      qos.setA2zPbs(upStreamQos.getPbs()/PBS_MULTIPLIER);
    }

    if (downStreamQos!= null && downStreamQos.getEnable()) {
      qos.setCacMode(Integer.getInteger(SCacMode.open.toString()));
      qos.setZ2aPolicing(Integer.getInteger(SQosPolicing.open.toString()));
      qos.setZ2aCir(downStreamQos.getCir());
      qos.setZ2aPir(downStreamQos.getPir());
      qos.setZ2aCbs(downStreamQos.getCbs()/CBS_MULTIPLIER);
      qos.setZ2aPbs(downStreamQos.getPbs()/PBS_MULTIPLIER);
    }

    return qos;
  }
}
