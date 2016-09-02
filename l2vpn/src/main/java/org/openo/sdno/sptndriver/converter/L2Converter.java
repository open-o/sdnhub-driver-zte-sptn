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
import org.openo.sdno.sptndriver.enums.south.SElineSncType;
import org.openo.sdno.sptndriver.enums.south.SInterConnectionMode;
import org.openo.sdno.sptndriver.enums.south.ac.SAcRole;
import org.openo.sdno.sptndriver.enums.south.pw.SPwRole;
import org.openo.sdno.sptndriver.enums.south.pw.SSnSupport;
import org.openo.sdno.sptndriver.models.north.NL2Ac;
import org.openo.sdno.sptndriver.models.north.NL2Access;
import org.openo.sdno.sptndriver.models.north.NL2Acs;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NPw;
import org.openo.sdno.sptndriver.models.north.NPws;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SEline;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoint;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoints;
import org.openo.sdno.sptndriver.models.south.SSncPw;
import org.openo.sdno.sptndriver.models.south.SSncPws;
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
  public static SCreateElineAndTunnels L2ToElineTunnerCreator(NL2Vpn l2vpn) {
    SCreateElineAndTunnels createElineAndTunnels = new SCreateElineAndTunnels();
    createElineAndTunnels.setSncEline(L2ToEline(l2vpn, l2vpn.getId()));
    createElineAndTunnels
        .setSncTunnelCreatePolicy(SSncTunnelCreatePolicyInitiator.initTunnelPolicy(l2vpn));
    return createElineAndTunnels;
  }

  /**
   * convert L2vpn to Eline.
   *
   * @param nl2Vpn   NBI L2vpn.
   * @param SElineId Eline uuid.
   * @return SBI Eline.
   */
  public static SEline L2ToEline(NL2Vpn nl2Vpn, String SElineId) {
    if (nl2Vpn == null) {
      LOGGER.error("input l2vpn is null.");
      return null;
    }

    SEline sEline = new SEline();
    sEline.setId(SElineId);
    sEline.setName(new String());
    sEline.setUserLabel(nl2Vpn.getName());
    sEline.setParentNcdId(null);
    sEline.setAdminStatus(AdminStatusEnum.getIndex(nl2Vpn.getAdminStatus()));
    sEline.setOperateStatus(OperateStatusEnum.getIndex(nl2Vpn.getOperStatus()));
    sEline.setSncType(Integer.getInteger(SElineSncType.SIMPLE.toString()));
    sEline.setInterconnectionMode(Integer.getInteger(SInterConnectionMode.UNI_UNI.toString()));
    sEline.setIngressEndPoints(NToS(nl2Vpn.getAcs(), true));
    sEline.setEgressEndPoints(NToS(nl2Vpn.getAcs(), false));
    boolean hasProtect = hasBackupPw(nl2Vpn.getPws());
    sEline.setSncSwitch(
        SSncSwitchInitiator.initPwSncSwitch(SElineId, hasProtect));
    sEline.setSncPws(initPws(nl2Vpn));
    return sEline;
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
  private static SServiceEndPoints NToS(NL2Acs acList, boolean isIngress) {
    if (acList == null
        || acList.getAc() == null
        || acList.getAc().size() != 2) {
      LOGGER.error("input acList size is not 2.");
      return null;
    }
    SServiceEndPoints epList = new SServiceEndPoints();
    if (isIngress) {
      epList.add(NToS(acList.getAc().get(0)));
    } else {
      epList.equals(NToS(acList.getAc().get(1)));
    }

    return epList;
  }

  /**
   * Convert NBI AC to SBI AC.
   *
   * @param ac NBI AC.
   * @return SBI AC.
   */
  private static SServiceEndPoint NToS(NL2Ac ac) {
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
      ep.setAccessType(AccessTypeEnum.getIndex(nl2Access.getAccessType()));
      ep.setDot1qVlanBitmap(nl2Access.getDot1qVlanBitmap());
      ep.setQinqCvlanBitmap(nl2Access.getQinqCvlanBitmap());
      ep.setQinqSvlanBitmap(nl2Access.getQinqSvlanBitmap());
      String accessAction = nl2Access.getAccessAction();
      ep.setAccessAction(AccessActionEnum.getIndex(accessAction));
      if (accessAction != null) {
        if (accessAction.equals(AccessActionEnum.PUSH.getName())) {
          ep.setAccessVlanId(nl2Access.getPushVlanId());
        } else if (accessAction.equals(AccessActionEnum.SWAP.getName())) {
          ep.setAccessVlanId(nl2Access.getSwapVlanId());
        } else {
          LOGGER.debug(
              "No need to config access vlan id since access action is " + accessAction + ".");
        }
      }

      ep.setQos(SQosInitiator
                    .initAcQos(ep.getId(), ac.getUpstreamBandwidth(), ac.getDownstreamBandwidth()));
    }

    ep.setRole(Integer.getInteger(SAcRole.MASTER.toString()));

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
      sncPw.setRole(Integer.getInteger(SPwRole.MASTER.toString()));
      sncPw.setEncaplateType(EncapsulateTypeEnum.getIndex(l2Vpn.getEncapsulation().toString()));
      sncPw.setIngressNeId(pwA.getNeId());
      sncPw.setEgressNeId(pwZ.getNeId());
      sncPw.setDestinationIp(pwA.getPeerAddress());
      sncPw.setSourceIp(pwZ.getPeerAddress());
      sncPw.setAdminStatus(AdminStatusEnum.getIndex(l2Vpn.getAdminStatus()));
      sncPw.setOperateStatus(OperateStatusEnum.getIndex(l2Vpn.getOperStatus()));
      sncPw.setCtrlWordSupport(CtrlWordEnum.getIndex(l2Vpn.getCtrlWordType().toString()));
      sncPw.setSnSupport(Integer.getInteger(SSnSupport.NOT_SUPPORT.toString()));
      sncPw.setVccvType(0);
      sncPw.setConnAckType(0);
      sncPw.setQos(SQosInitiator.initCacClosedQos(sncPw.getId()));
      sncPw.setOam(SOamInitiator.initOam(sncPw.getId()));
      pwList.add(sncPw);
    }

    return pwList;
  }

}
