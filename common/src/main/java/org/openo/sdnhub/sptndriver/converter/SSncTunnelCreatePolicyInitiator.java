/*
 * Copyright 2016-2017 ZTE Corporation.
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

package org.openo.sdnhub.sptndriver.converter;

import org.openo.sdnhub.sptndriver.exception.ParamErrorException;
import org.openo.sdnhub.sptndriver.models.north.NTunnelService;
import org.openo.sdnhub.sptndriver.models.south.SAdminStatus;
import org.openo.sdnhub.sptndriver.models.south.SSncTunnelCreatePolicy;

/**
 * The class to initiate tunnel create policy..
 */
public class SSncTunnelCreatePolicyInitiator {

    /**
     * Initialize tunnel create policy.
     *
     * @param tunnelService NBI tunnel service.
     * @return SBI Tunnel create policy.
     */
    public static SSncTunnelCreatePolicy initTunnelPolicy(NTunnelService tunnelService)
        throws ParamErrorException {
        if (tunnelService == null) {
            throw new ParamErrorException("Input tunnel service is null.");
        }
        SSncTunnelCreatePolicy tunnelCreatePolicy = new SSncTunnelCreatePolicy();
        tunnelCreatePolicy.setName(null);
        // Can find tunnel's user label in NBI, so let controller config user label
        tunnelCreatePolicy.setUserLabel(null);
        // todo tunnelCreatePolicy.setTenantId(nl2Vpn.getTenantId());
        tunnelCreatePolicy.setCreater(null);
        tunnelCreatePolicy.setParentNcdId(null);
        tunnelCreatePolicy.setDirection(SSncTunnelCreatePolicy.DirectionEnum.BIDIRECTION);
        tunnelCreatePolicy.setType(SncType.LINE_MPLS.toString());
        tunnelCreatePolicy.setQos(SQosInitiator.initQos(tunnelService.getMplsTe()));
        tunnelCreatePolicy.setAdminStatus(SAdminStatus.UP);

        tunnelCreatePolicy.setSncSwitch(
            SSncSwitchInitiator.initLspSncSwitch(tunnelService.getMplsTe()));
        if (SSncSwitchInitiator.hasProtect(tunnelService.getMplsTe())) {
            tunnelCreatePolicy.setLspOam(SOamInitiator.initOam(null));
        } else {
            tunnelCreatePolicy.setLspOam(null);
        }
        return tunnelCreatePolicy;
    }

    /**
     * Tunnel type in tunnel create policy.
     */
    private enum SncType {
        LINE_MPLS(1),
        RING_MPLS(2);
        private Integer value;

        SncType(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
