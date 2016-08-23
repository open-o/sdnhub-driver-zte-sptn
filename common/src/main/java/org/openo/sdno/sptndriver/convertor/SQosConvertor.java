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
import org.openo.sdno.sptndriver.models.south.SQOS;

public class SQosConvertor {
   public static SQOS initQos(NMplsTePolicy policy) {
     SQOS qos = new SQOS();
     // tunnel is created by controller, so the id is not known yet, controller should fill this field
     qos.setBelongedId(null);
     qos.setTunnelMode(Integer.getInteger(STunnelMode.pipeline.toString()));

     qos.setConvgMode(Integer.getInteger(SConvergeMode.not_converge.toString()));
     // todo Whether support auto_adjust, equals to bandwidthMode in mplsTePolicy?
     qos.setTrafficAdjMode(Integer.getInteger(STrafficAdjustMode.not_adjust.toString()));
     // todo not sure how to config pir, cbs and pbs, need test
     if (policy != null && policy.getBandwidth() != null)
     {
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
}
