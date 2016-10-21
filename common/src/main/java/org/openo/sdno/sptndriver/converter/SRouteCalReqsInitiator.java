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

import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NParticularConstraint;
import org.openo.sdno.sptndriver.models.north.NTunnelService;
import org.openo.sdno.sptndriver.models.south.SCalculateConstraint;
import org.openo.sdno.sptndriver.models.south.SNeId;
import org.openo.sdno.sptndriver.models.south.SRouteCalReq;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqContainer;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElement;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElementLeftneids;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElementRightneids;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqs;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class to init route calculate request.
 */
public class SRouteCalReqsInitiator {

  private static final Logger LOGGER = LoggerFactory.getLogger(SRouteCalReqsInitiator.class);
  private static final String WORK_SEQUENCE_NO = "1";
  private static final String PROTECTION_SEQUENCE_NO = "2";

  /**
   * Init LSP route calculate request of L2.
   *
   * @param l2vpn L2vpn create parameters.
   * @return LSP route calculate request.
   */
  public static SRouteCalReqsInput initElineLspCalRoute(NL2Vpn l2vpn)
      throws ParamErrorException {
    if (l2vpn == null) {
      throw new ParamErrorException("Input l2vpn is null.");
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
    if (mplsTePolicy == null
        && tunnelService != null
        && tunnelService.getMplsTe() != null) {
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
                                                         String egressNe)
      throws ParamErrorException {
    if (ingressNe == null || egressNe == null) {
      throw new ParamErrorException("Ingress ne or egress ne is null.");
    }

    SRouteCalReqs routeCalReqs = new SRouteCalReqs();
    boolean hasBackupLsp = hasProtection(mplsTePolicy);
    SRouteCalReqContainer routeCalReqContainer
        = initElineLspCalRoute(mplsTePolicy, ingressNe, egressNe, hasBackupLsp);
    routeCalReqs.setRouteCalReqs(new SRouteCalReq());
    routeCalReqs.getRouteCalReqs().getRouteCalReq().add(routeCalReqContainer);

    SRouteCalReqsInput routeCalReqsInput = new SRouteCalReqsInput();
    routeCalReqsInput.setInput(routeCalReqs);
    return routeCalReqsInput;
  }

  private static SRouteCalReqContainer initElineLspCalRoute(NMplsTePolicy mplsTePolicy,
                                                            String ingressNe,
                                                            String egressNe,
                                                            boolean hasProtect) {
    SRouteCalReqElement routeCalReq = new SRouteCalReqElement();
    routeCalReq.setSequenceNo(WORK_SEQUENCE_NO);

    routeCalReq.setCalculatePolicy(SRouteCalReqElement.CalculatePolicyEnum.LOCAL_PROTECTION);
    routeCalReq.setCalculateMode(SRouteCalReqElement.CalculateModeEnum.SIMPLE);
    if (isBestEffort(mplsTePolicy)) {
      routeCalReq.setCalculateType(SRouteCalReqElement.CalculateTypeEnum.BESTEFFORT_SEPARATE);
    } else {
      routeCalReq.setCalculateType(SRouteCalReqElement.CalculateTypeEnum.STRICT_SEPARATE);
    }

    routeCalReq.setCalculateInterconnectionMode(SRouteCalReqElement
        .CalculateInterconnectionModeEnum.UNI_UNI);
    routeCalReq.setLayerRate(SRouteCalReqElement.LayerRateEnum.LSP);
    SNeId leftNe = new SNeId();
    leftNe.setNeId(ingressNe);
    SNeId rightNe = new SNeId();
    rightNe.setNeId(egressNe);
    routeCalReq.setLeftNeIds(new SRouteCalReqElementLeftneids());
    routeCalReq.getLeftNeIds().getLeftNeId().add(leftNe);
    routeCalReq.setRightNeIds(new SRouteCalReqElementRightneids());
    routeCalReq.getRightNeIds().getRightNeId().add(rightNe);
    SCalculateConstraint calculateConstraint = new SCalculateConstraint();
    calculateConstraint.setBandwidth(mplsTePolicy.getBandwidth().toString());
    calculateConstraint.setCalPolicy(SCalculateConstraint.CalPolicyEnum.BANDWIDTH_BALANCING);
    routeCalReq.setWorkCalculateConstraint(calculateConstraint);
    if (hasProtect) {
      routeCalReq.setProtectCalculateConstraint(calculateConstraint);
    }
    SRouteCalReqContainer routeCalReqContainer = new SRouteCalReqContainer();
    routeCalReqContainer.setRouteCalReqContainer(routeCalReq);
    return routeCalReqContainer;
  }

  private static boolean hasProtection(NMplsTePolicy mplsTePolicy) {
    return mplsTePolicy != null && mplsTePolicy.getPathProtectPolicy() != null;
  }

  private static boolean isBestEffort(NMplsTePolicy mplsTePolicy) {
    return mplsTePolicy != null && mplsTePolicy.getBesteffort() != null;
  }


}
