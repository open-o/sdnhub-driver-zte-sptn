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

import org.openo.sdno.sptndriver.enums.south.SDirection;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SAdminStatus;
import org.openo.sdno.sptndriver.models.south.SSncTunnelCreatePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  The class to initiate tunnel create policy..
 */
public class SSncTunnelCreatePolicyInitiator {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SSncTunnelCreatePolicyInitiator.class);

  /**
   *  Init tunnel create policy of Eline.
   * @param nl2Vpn L2vpn.
   * @return Tunnel create policy.
   */
  public static SSncTunnelCreatePolicy initTunnelPolicy(NL2Vpn nl2Vpn) {
    if (nl2Vpn == null || nl2Vpn.getTunnelService() == null) {
      LOGGER.error("l2vpn or tunnelService is null.");
      return null;
    }
    SSncTunnelCreatePolicy tunnelCreatePolicy = new SSncTunnelCreatePolicy();
    tunnelCreatePolicy.setName(null);
    // todo where to find the user label?
    tunnelCreatePolicy.setUserLabel(null);
    // todo tunnelCreatePolicy.setTenantId(nl2Vpn.getTenantId());
    tunnelCreatePolicy.setCreater(null);
    tunnelCreatePolicy.setParentNcdId(null);
    tunnelCreatePolicy.setDirection(SSncTunnelCreatePolicy.DirectionEnum.BIDIRECTION);
    tunnelCreatePolicy.setType(SncType.line_mpls.toString());
    // todo tunnelCreatePolicy.setIsShared(false);
    tunnelCreatePolicy.setQos(SQosInitiator.initQos(nl2Vpn.getTunnelService().getMplsTe()));
    tunnelCreatePolicy.setAdminStatus(SAdminStatus.UP);
    tunnelCreatePolicy.setLspOam(SOamInitiator.initOam(null));
    tunnelCreatePolicy.setSncSwitch(
        SSncSwitchInitiator.initLspSncSwitch(nl2Vpn.getTunnelService().getMplsTe()));
    return tunnelCreatePolicy;
  }

  /**
   *  Tunnel type in tunnel create policy.
   */
  private enum SncType {
    line_mpls(1),
    ring_mpls(2);
    private Integer value;

    SncType(Integer value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }
  }
}
