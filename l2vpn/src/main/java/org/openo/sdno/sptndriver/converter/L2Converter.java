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

import org.openo.sdno.sptndriver.enums.AdminStatusEnum;
import org.openo.sdno.sptndriver.enums.OperateStatusEnum;
import org.openo.sdno.sptndriver.enums.ac.AccessActionEnum;
import org.openo.sdno.sptndriver.enums.ac.AccessTypeEnum;
import org.openo.sdno.sptndriver.enums.pw.CtrlWordEnum;
import org.openo.sdno.sptndriver.enums.pw.EncapsulateTypeEnum;
import org.openo.sdno.sptndriver.enums.south.pw.SSnSupport;
import org.openo.sdno.sptndriver.models.north.NL2Ac;
import org.openo.sdno.sptndriver.models.north.NL2Access;
import org.openo.sdno.sptndriver.models.north.NL2Acs;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NPw;
import org.openo.sdno.sptndriver.models.north.NPws;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SEgressEps;
import org.openo.sdno.sptndriver.models.south.SEline;
import org.openo.sdno.sptndriver.models.south.SIngressEps;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoint;
import org.openo.sdno.sptndriver.models.south.SSncPw;
import org.openo.sdno.sptndriver.models.south.SSncPws;
import org.openo.sdno.sptndriver.models.south.SSncType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class to convert from L2vpn to Eline.
 */
public class L2Converter {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(L2Converter.class);

  /**
   * Convert create L2vpn to Eline.
   *
   * @param l2vpn L2vpn creating parameters in NBI.
   * @return Eline and tunnel creating parameters in SBI.
   */
  public static SCreateElineAndTunnelsInput convertL2ToElineTunnerCreator(NL2Vpn l2vpn) {
    SCreateElineAndTunnels createElineAndTunnels = new SCreateElineAndTunnels();
    createElineAndTunnels.setSncEline(l2ToEline(l2vpn, l2vpn.getId()));
    createElineAndTunnels
        .setSncTunnelCreatePolicy(SSncTunnelCreatePolicyInitiator.initTunnelPolicy(l2vpn));
    SCreateElineAndTunnelsInput createElineAndTunnelsInput = new SCreateElineAndTunnelsInput();
    createElineAndTunnelsInput.setInput(createElineAndTunnels);
    return createElineAndTunnelsInput;
  }

  /**
   * convert L2vpn to Eline.
   *
   * @param nl2Vpn   NBI L2vpn.
   * @param southElineId Eline uuid.
   * @return SBI Eline.
   */
  private static SEline l2ToEline(NL2Vpn nl2Vpn, String southElineId) {
    if (nl2Vpn == null) {
      LOGGER.error("input l2vpn is null.");
      return null;
    }

    SEline eline = new SEline();
    eline.setId(southElineId);
    eline.setName(null);
    eline.setUserLabel(nl2Vpn.getName());
    eline.setParentNcdId(null);
    eline.setAdminStatus(AdminStatusEnum.convertNbiToSbi(nl2Vpn.getAdminStatus()));
    eline.setOperateStatus(OperateStatusEnum.convertNbiToSbi(nl2Vpn.getOperStatus()));
    eline.setSncType(SSncType.SIMPLE);
    eline.setInterconnectionMode(SEline.InterconnectionModeEnum.UNI_UNI);

    SIngressEps ingressEps = new SIngressEps();
    ingressEps.setIngressEndPoint(northToSouth(nl2Vpn.getAcs(), true));
    eline.setIngressEndPoints(ingressEps);

    SEgressEps egressEps = new SEgressEps();
    egressEps.setEgressEndPoint(northToSouth(nl2Vpn.getAcs(), false));
    eline.setEgressEndPoints(egressEps);

    boolean hasProtect = hasBackupPw(nl2Vpn.getPws());
    eline.setSncSwitch(
        SSncSwitchInitiator.initPwSncSwitch(southElineId, hasProtect));
    eline.setSncPws(initPws(nl2Vpn));
    return eline;
  }

  /**
   * Get SBI Eline UUID from NBI L2vpn UUID.Currently controller Eline id is equal to Eline id in
   * SDN-O, may get id from map in the future.
   *
   * @param northElineId NBI L2vpn UUID.
   * @return SBI Eline UUID.
   */
  public static String getSouthElineId(String northElineId) {
    return northElineId;
  }

  /**
   * Convert ingress or egress ac list, only support 2 ACs.
   *
   * @param acList    NBI AC list.
   * @param isIngress Whether is ingress or egress AC.
   * @return SBI AC list, if input NBI AC list size is not 2, return null.
   */
  private static SServiceEndPoint northToSouth(NL2Acs acList, boolean isIngress) {
    if (acList == null
        || acList.getAc() == null
        || acList.getAc().size() != 2) {
      LOGGER.error("input acList size is not 2.");
      return null;
    }

    if (isIngress) {
      return northToSouth(acList.getAc().get(0));
    } else {
      return northToSouth(acList.getAc().get(1));
    }
  }

  /**
   * Convert NBI AC to SBI AC.
   *
   * @param ac NBI AC.
   * @return SBI AC.
   */
  private static SServiceEndPoint northToSouth(NL2Ac ac) {
    if (ac == null) {
      LOGGER.error("input ac is null.");
      return null;
    }
    SServiceEndPoint ep = new SServiceEndPoint();
    ep.setId(ac.getId());
    ep.setNeId(ac.getNeId());
    ep.setLtpId(ac.getLtpId());

    NL2Access nl2Access = ac.getL2Access();
    if (nl2Access != null) {
      ep.setAccessType(AccessTypeEnum.convertNbiToSbi(nl2Access.getAccessType()));
      ep.setDot1qVlanBitmap(nl2Access.getDot1qVlanBitmap().toString());
      ep.setQinqCvlanBitmap(nl2Access.getQinqCvlanBitmap().toString());
      ep.setQinqSvlanBitmap(nl2Access.getQinqSvlanBitmap().toString());
      String accessAction = nl2Access.getAccessAction();
      ep.setAccessAction(AccessActionEnum.convertNbiToSbi(accessAction));
      if (accessAction != null) {
        if (accessAction.equals(AccessActionEnum.PUSH.getNorth())) {
          ep.setAccessVlanId(nl2Access.getPushVlanId().toString());
        } else if (accessAction.equals(AccessActionEnum.SWAP.getNorth())) {
          ep.setAccessVlanId(nl2Access.getSwapVlanId().toString());
        } else {
          LOGGER.debug(
              "No need to config access vlan id since access action is " + accessAction + ".");
        }
      }

      ep.setQos(SQosInitiator
                    .initAcQos(ep.getId(), ac.getUpstreamBandwidth(), ac.getDownstreamBandwidth()));
    }

    ep.setRole(SServiceEndPoint.RoleEnum.MASTER);

    return ep;
  }

  /**
   * Whether has backup PW.
   *
   * @param pwList PW list.
   * @return true when one of the PW is backup PW, return false when PW list is null or empty.
   */
  private static boolean hasBackupPw(NPws pwList) {
    if (pwList == null) {
      LOGGER.error("pwList is empty.");
      return false;
    }
    for (NPw pw : pwList.getPws()) {
      if (pw.getProtectionRole().equals(NPw.ProtectionRoleEnum.BACKUP)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Init PW from NBI L2vpn, PW protection is not supported now,so there is only 2 NEs, NE_A and
   * NE_Z and pw role is always MASTER.
   *
   * @param l2Vpn NBI L2vpn.
   * @return SBI PW list.
   */
  private static SSncPws initPws(NL2Vpn l2Vpn) {
    if (l2Vpn == null || l2Vpn.getPws() == null) {
      LOGGER.error("l2vpn or pwList is null.");
      return null;
    }

    if (l2Vpn.getPws().getPws().size() != 2) {
      LOGGER.error("l2vpn or pwList size is not 2 or 3.");
      return null;
    }
    SSncPws pwList = new SSncPws();
    if (l2Vpn.getPws().getPws().size() == 2) {
      NPw pwA = l2Vpn.getPws().getPws().get(0);
      NPw pwZ = l2Vpn.getPws().getPws().get(1);

      SSncPw sncPw = new SSncPw();
      sncPw.setId(l2Vpn.getPws().getUuid());
      sncPw.setName(null);
      sncPw.setUserLabel(pwA.getName());
      sncPw.setRole(SSncPw.RoleEnum.MASTER);
      sncPw.setEncaplateType(
          EncapsulateTypeEnum.convertNbiToSbi(l2Vpn.getEncapsulation().toString()));
      sncPw.setIngressNeId(pwA.getNeId());
      sncPw.setEgressNeId(pwZ.getNeId());
      sncPw.setDestinationIp(pwA.getPeerAddress());
      sncPw.setSourceIp(pwZ.getPeerAddress());
      sncPw.setAdminStatus(AdminStatusEnum.convertNbiToSbi(l2Vpn.getAdminStatus()));
      sncPw.setOperateStatus(OperateStatusEnum.convertNbiToSbi(l2Vpn.getOperStatus()));
      sncPw.setCtrlWordSupport(
          CtrlWordEnum.getIndex(l2Vpn.getCtrlWordType().toString()).toString());
      sncPw.setSnSupport(SSnSupport.NOT_SUPPORT.toString());
      sncPw.setVccvType("0");
      sncPw.setConnAckType("0");
      sncPw.setQos(SQosInitiator.initCacClosedQos(sncPw.getId()));
      sncPw.setOam(SOamInitiator.initOam(sncPw.getId()));
      pwList.setSncPw(sncPw);
    }

    return pwList;
  }

}
