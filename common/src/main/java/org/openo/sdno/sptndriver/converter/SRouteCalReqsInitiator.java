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

import org.openo.sdno.sptndriver.enums.south.SInterConnectionMode;
import org.openo.sdno.sptndriver.enums.south.SSncLayerRate;
import org.openo.sdno.sptndriver.enums.south.routecal.SRouteCalculateMode;
import org.openo.sdno.sptndriver.enums.south.routecal.SRouteCalculatePolicy;
import org.openo.sdno.sptndriver.enums.south.routecal.SRouteConstraintPolicy;
import org.openo.sdno.sptndriver.enums.south.routecal.SRouteSeparate;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NParticularConstraint;
import org.openo.sdno.sptndriver.models.north.NTunnelService;
import org.openo.sdno.sptndriver.models.south.SCalculateConstraint;
import org.openo.sdno.sptndriver.models.south.SNeId;
import org.openo.sdno.sptndriver.models.south.SRouteCalReq;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqs;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SRouteCalReqsInitiator {

  private static final Logger LOGGER = LoggerFactory.getLogger(SRouteCalReqsInitiator.class);
  private static final String WORK_SEQUENCE_NO = "1";
  private static final String PROTECTION_SEQUENCE_NO = "2";

  public static SRouteCalReqsInput initElineLspCalRoute(NL2Vpn l2vpn) {
    if (l2vpn == null) {
      LOGGER.error("Input l2vpn is null.");
      return null;
    }

    NMplsTePolicy mplsTePolicy = null;
    String ingressNe = null;
    String egressNe = null;
    // Try to get mplsTePolicy from particular constraints.
    NTunnelService tunnelService = l2vpn.getTunnelService();
    if (tunnelService != null
        && tunnelService.getParticularConstraints() != null
        && tunnelService.getParticularConstraints().getParticularConstraint() != null
        && !tunnelService.getParticularConstraints().getParticularConstraint().isEmpty()) {
      NParticularConstraint
          particularConstraint =
          tunnelService.getParticularConstraints().getParticularConstraint().get(0);
      mplsTePolicy = particularConstraint.getMplsTe();
      ingressNe = particularConstraint.getIngressNe();
      egressNe = particularConstraint.getEgressNe();
    }
    // If get mplsTePolicy from particular constraints failed, try to get it from tunnel service.
    if (mplsTePolicy == null && tunnelService.getMplsTe() != null) {
      mplsTePolicy = tunnelService.getMplsTe();
    }
    // if init ingress NE or egress NE failed, try to get the information from ACs.
    if (ingressNe == null || egressNe == null) {
      if (l2vpn.getAcs() != null
          && l2vpn.getAcs().getAc() != null
          && l2vpn.getAcs().getAc().size() == 2) {
        ingressNe = l2vpn.getAcs().getAc().get(0).getNeId();
        egressNe = l2vpn.getAcs().getAc().get(1).getNeId();
      }
    }
    // if init ingress NE or egress NE failed, try to get the information from PWs.
    if (ingressNe == null || egressNe == null) {
      if (l2vpn.getPws() != null
          && l2vpn.getPws().getPws() != null
          && l2vpn.getPws().getPws().size() == 2) {
          ingressNe = l2vpn.getPws().getPws().get(0).getNeId();
          egressNe = l2vpn.getPws().getPws().get(1).getNeId();
      }
    }

    return initElineLspCalRoute(mplsTePolicy, ingressNe, egressNe);
  }

  private static SRouteCalReqsInput initElineLspCalRoute(NMplsTePolicy mplsTePolicy,
                                                         String ingressNe,
                                                         String egressNe) {
    if (ingressNe == null || egressNe == null) {
      LOGGER.error("Ingress NE or egress NE is null.");
      return null;
    }
    SRouteCalReqsInput routeCalReqsInput = new SRouteCalReqsInput();
    SRouteCalReqs routeCalReqs = new SRouteCalReqs();
    boolean hasBackupLsp = hasProtection(mplsTePolicy);
    SRouteCalReq workRouteCalReq = new SRouteCalReq();
    workRouteCalReq.setSequenceNo(WORK_SEQUENCE_NO);
    workRouteCalReq.setCalculatePolicy(SRouteCalculatePolicy.LOCAL_PROTECTION.getValue());
    workRouteCalReq.setCalculateMode(SRouteCalculateMode.ONE_ONE.getValue());
    if (isBestEffort(mplsTePolicy)) {
      workRouteCalReq.setCalculateType(SRouteSeparate.BEST_EFFORT_SEPARATE.getValue());
    } else {
      workRouteCalReq.setCalculateType(SRouteSeparate.STRICT_SEPARATE.getValue());
    }

    workRouteCalReq.setCalculateInterconnectionMode(SInterConnectionMode.UNI_UNI.getValue());
    workRouteCalReq.setLayerRate(SSncLayerRate.LSP.getValue());
    SNeId leftNe = new SNeId();
    leftNe.setNeId(ingressNe);
    SNeId rightNe = new SNeId();
    rightNe.setNeId(egressNe);
    workRouteCalReq.getLeftNeIds().add(leftNe);
    workRouteCalReq.getRightNeIds().add(rightNe);
    SCalculateConstraint workCalcConstraint = new SCalculateConstraint();
    workCalcConstraint.setBandwidth(mplsTePolicy.getBandwidth());
  //  workCalcConstraint.setCalPolicy(SRouteConstraintPolicy.BANDWIDTH_BALANCING.getValue());
  //  workRouteCalReq.setProtectCalculateConstraint();
    return routeCalReqsInput;
  }

  private static boolean hasProtection(NMplsTePolicy mplsTePolicy) {
    if (mplsTePolicy == null) {
      return false;
    }
    return mplsTePolicy != null
           && mplsTePolicy.getPathProtectPolicy() != null;
  }

  private static boolean isBestEffort(NMplsTePolicy mplsTePolicy) {
    return mplsTePolicy != null && mplsTePolicy.getBesteffort();
  }

}
