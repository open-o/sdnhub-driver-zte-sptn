/*
 * Copyright 2016 ZTE Corporation.
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
import org.openo.sdno.sptndriver.utils.MathUtil;

/**
 * The class to initiate QoS parameters.
 */
public class SQosInitiator {

    /**
     * NBI CBS in bytes and SBI CBS in KBytes.
     */
    private static final int CBS_MULTIPLIER = 1000;
    /**
     * NBI PBS in bytes and SBI PBS in KBytes.
     */
    private static final int PBS_MULTIPLIER = 1000;
    /**
     * Default CBS in KBytes.
     */
    private static final String DEFAULT_CBS = "100";
    /**
     * Default PBS in KBytes.
     */
    private static final String DEFAULT_PBS = "100";

    private SQosInitiator(){}

    /**
     * Initialize LSP QoS parameters according to MplsTePolicy.
     *
     * @param policy MplsTePolicy in NBI.
     * @return LSP QoS in SBI.
     */
    public static SQos initQos(NMplsTePolicy policy) {
        SQos qos = new SQos();
        qos.setBelongedId(null);
        qos.setTunnelMode(SQos.TunnelModeEnum.PIPELINE);
        qos.setConvgMode(SConvergeMode.NOT_CONVERGE.toString());
        qos.setTrafficAdjMode(STrafficAdjustMode.AUTO_ADJUST.toString());

        if (policy != null && policy.getBandwidth() != null) {
            qos.setCacMode(SCacMode.OPEN.toString());
            qos.setA2zPolicing(SQosPolicing.OPEN.toString());
            qos.setZ2aPolicing(SQosPolicing.OPEN.toString());
            qos.setA2zCir(policy.getBandwidth().toString());
            qos.setZ2aCir(policy.getBandwidth().toString());
            qos.setA2zPir(policy.getBandwidth().toString());
            qos.setZ2aPir(policy.getBandwidth().toString());
            qos.setA2zCbs(DEFAULT_CBS);
            qos.setZ2aCbs(DEFAULT_CBS);
            qos.setA2zPbs(DEFAULT_PBS);
            qos.setZ2aPbs(DEFAULT_PBS);
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
     * Initialize QoS in which CAC mode and policing is closed.
     *
     * @param belongedId The UUID of object(PW or AC) which the QoS is belonged to.
     * @return QoS.
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
        qos.setTrafficClass(org.openo.sdno.sptndriver.models.south.STrafficClass.CS7);
        return qos;
    }

    /**
     * Initialize AC QoS.
     *
     * @param acId          AC UUID
     * @param upStreamQos   Upstream AC QoS.
     * @param downStreamQos Downstream AC QoS.
     * @return AC QoS.
     */
    public static SQos initAcQos(String acId, NQosIfCar upStreamQos, NQosIfCar downStreamQos) {
        SQos qos = initCacClosedQos(acId);
        if (upStreamQos != null
            && upStreamQos.getEnable() != null
            && upStreamQos.getEnable()) {
            qos.setCacMode(SCacMode.OPEN.toString());
            qos.setA2zPolicing(SQosPolicing.OPEN.toString());
            qos.setA2zCir(getString(upStreamQos.getCir()));
            qos.setA2zPir(getString(upStreamQos.getPir()));
            qos.setA2zCbs(getSouthCbs(upStreamQos.getCbs()));
            qos.setA2zPbs(getSouthPbs(upStreamQos.getPbs()));
        }

        if (downStreamQos != null
            && downStreamQos.getEnable() != null
            && downStreamQos.getEnable()) {
            qos.setCacMode(SCacMode.OPEN.toString());
            qos.setZ2aPolicing(SQosPolicing.OPEN.toString());
            qos.setZ2aCir(getString(downStreamQos.getCir()));
            qos.setZ2aPir(getString(downStreamQos.getPir()));
            qos.setZ2aCbs(getSouthCbs(downStreamQos.getCbs()));
            qos.setZ2aPbs(getSouthPbs(downStreamQos.getPbs()));
        }

        return qos;
    }

    private static String getSouthCbs(Long northCbs) {
        if (northCbs == null) {
            return null;
        }
        return Long.toString(MathUtil.ceil((float) northCbs, (float) CBS_MULTIPLIER));
    }

    private static String getSouthPbs(Long northPbs) {
        if (northPbs == null) {
            return null;
        }
        return Long.toString(MathUtil.ceil((float) northPbs, (float) PBS_MULTIPLIER));
    }

    private static String getString(Long longValue) {
        if (longValue == null) {
            return null;
        }
        return longValue.toString();
    }

}
