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

import org.openo.sdno.sptndriver.convertor.ac.AccessActionConvertor;
import org.openo.sdno.sptndriver.convertor.ac.AccessTypeConvertor;
import org.openo.sdno.sptndriver.enums.north.ac.NACAccessAction;
import org.openo.sdno.sptndriver.enums.south.SElineSncType;
import org.openo.sdno.sptndriver.enums.south.SInterConnectionMode;
import org.openo.sdno.sptndriver.enums.south.ac.SACRole;
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

public class L2Convertor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(L2Convertor.class);

  public static SCreateElineAndTunnels L2ToElineTunnerCreator(NL2Vpn l2vpn) {
    SCreateElineAndTunnels createElineAndTunnels = new SCreateElineAndTunnels();
    // todo: check L2 LCM must assign the L2 ID
    createElineAndTunnels.setSncEline(L2ToEline(l2vpn, l2vpn.getId()));
    createElineAndTunnels
        .setSncTunnelCreatePolicy(SSncTunnelCreatePolicyConvertor.initTunnelPolicy(l2vpn));
    return createElineAndTunnels;
  }

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
    sEline.setAdminStatus(AdminStatusConvertor.NToS(nl2Vpn.getAdminStatus()));
    sEline.setOperateStatus(OperateStatusConvertor.NToS(nl2Vpn.getOperStatus()));
    sEline.setSncType(Integer.getInteger(SElineSncType.Simple.toString()));
    sEline.setInterconnectionMode(Integer.getInteger(SInterConnectionMode.uni_uni.toString()));
    sEline.setIngressEndPoints(NToS(nl2Vpn.getAcs(), true));
    sEline.setEgressEndPoints(NToS(nl2Vpn.getAcs(), false));
    boolean hasProtect = hasBackupPw(nl2Vpn.getPws());
    sEline.setSncSwitch(
        SSncSwitchConvertor.initPwSncSwitch(SElineId, hasProtect));
    sEline.setSncPws(initPws(nl2Vpn, hasProtect));
    return sEline;
  }

  // todo: Currently controller Eline id is equal to eline id in sdno, may get id from map in the future
  public static String getSouthElineId(String northElineId) {
    return northElineId;
  }

  /**
   * Convert ingress or egress ac list, only support 2 ACs
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
      ep.setAccessType(AccessTypeConvertor.NToS(nl2Access.getAccessType()));
      ep.setDot1qVlanBitmap(nl2Access.getDot1qVlanBitmap());
      ep.setQinqCvlanBitmap(nl2Access.getQinqCvlanBitmap());
      ep.setQinqSvlanBitmap(nl2Access.getQinqSvlanBitmap());
      String accessAction = nl2Access.getAccessAction();
      ep.setAccessAction(AccessActionConvertor.NToS(accessAction));
      if (accessAction != null) {
        if (accessAction.equals(NACAccessAction.push.toString())) {
          ep.setAccessVlanId(nl2Access.getPushVlanId());
        } else if (accessAction.equals(NACAccessAction.swap.toString())) {
          ep.setAccessVlanId(nl2Access.getSwapVlanId());
        } else {
          LOGGER.debug(
              "No need to config access vlan id since access action is " + accessAction + ".");
        }
      }

      ep.setQos(SQosConvertor
                    .initAcQos(ep.getId(), ac.getUpstreamBandwidth(), ac.getDownstreamBandwidth()));
    }

    ep.setRole(Integer.getInteger(SACRole.master.toString()));

    return ep;
  }

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

  // todo; pw protection is not supported now,so there is only 2 NEs, NE_A and NE_Z and pw role is always master
  private static SSncPws initPws(NL2Vpn l2Vpn, boolean hasProtect) {
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
      sncPw.setRole(Integer.getInteger(SPwRole.master.toString()));
      sncPw.setEncaplateType(EncaplateTypeConvertor.NToS(l2Vpn.getEncapsulation().toString()));
      sncPw.setIngressNeId(pwA.getNeId());
      sncPw.setEgressNeId(pwZ.getNeId());
      sncPw.setDestinationIp(pwA.getPeerAddress());
      sncPw.setSourceIp(pwZ.getPeerAddress());
      sncPw.setAdminStatus(AdminStatusConvertor.NToS(l2Vpn.getAdminStatus()));
      sncPw.setOperateStatus(OperateStatusConvertor.NToS(l2Vpn.getOperStatus()));
      sncPw.setCtrlWordSupport(CtrlWordConvertor.NToS(l2Vpn.getCtrlWordType().toString()));
      sncPw.setSnSupport(Integer.getInteger(SSnSupport.not_support.toString()));
      sncPw.setVccvType(0);
      sncPw.setConnAckType(0);
      sncPw.setQos(SQosConvertor.initCacClosedQos(sncPw.getId()));
      sncPw.setOam(SOamConvertor.initOAM(sncPw.getId()));
      pwList.add(sncPw);
    }

    return pwList;
  }

}
