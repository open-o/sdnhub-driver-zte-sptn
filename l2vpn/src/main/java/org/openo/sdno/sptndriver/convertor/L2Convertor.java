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

import org.openo.sdno.sptndriver.models.north.NL2Ac;
import org.openo.sdno.sptndriver.models.north.NL2Acs;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NOverrideFlow;
import org.openo.sdno.sptndriver.models.north.NOverrideFlows;
import org.openo.sdno.sptndriver.models.north.NPw;
import org.openo.sdno.sptndriver.models.north.NPws;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SEline;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoint;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoints;
import org.openo.sdno.sptndriver.models.south.SSncPws;
import org.openo.sdno.sptndriver.enums.south.SElineSncType;
import org.openo.sdno.sptndriver.enums.south.SInterConnectionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class L2Convertor {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(L2Convertor.class);

  public static SCreateElineAndTunnels L2ToElineTunnerCreator(NL2Vpn l2vpn) {
    SCreateElineAndTunnels createElineAndTunnels = new SCreateElineAndTunnels();
    createElineAndTunnels.setSncEline(L2ToEline(l2vpn));
    return createElineAndTunnels;
  }

  public static SEline L2ToEline(NL2Vpn nl2Vpn) {
    if (nl2Vpn == null) {
      LOGGER.error("input l2vpn is null.");
      return null;
    }

    SEline sEline = new SEline();
    sEline.setId(getSouthElineId(nl2Vpn.getId()));
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
        SSncSwitchConvertor.initPwSncSwitch(nl2Vpn.getId(), hasProtect));
    sEline.setSncPws(initPws(nl2Vpn, hasProtect));
    return sEline;
  }

  // todo: Currently controller Eline id is equal to eline id in sdno, may get id from map in the future
  public static String getSouthElineId(String northElineId) {
    return northElineId;
  }

  // todo: API need update
  private static SServiceEndPoints NToS(NL2Acs acList, boolean isIngress) {
    if (acList == null) {
      LOGGER.error("input acList is null");
      return null;
    }

    SServiceEndPoints epList = new SServiceEndPoints();
    for (NL2Ac ac : acList.getAc()) {
      NOverrideFlows overflows = ac.getOverrideFlows();
      if (ac.getOverrideFlows() != null) {
        for (NOverrideFlow overFlow : overflows.getOverrideFlow()) {
          if (isIngress && overFlow.getDirection().equals("ingress")
              || !isIngress && overFlow.getDirection().equals("egress")) {
            epList.add(NToS(ac));
          }
        }
      }
    }
    return epList;
  }

  // todo: API need update
  private static SServiceEndPoint NToS(NL2Ac ac) {
    if (ac == null) {
      LOGGER.error("input ac is null.");
      return null;
    }
    SServiceEndPoint ep = new SServiceEndPoint();
    return ep;
  }

  private static boolean hasBackupPw(NPws pwList) {
    if (pwList == null) {
      LOGGER.error("pwList is empty.");
      return false;
    }
    for (NPw pw : pwList.getPws()) {
      if (pw.getProtectionRole().equals("backup")) {
        return true;
      }
    }
    return false;
  }
  // todo; NE ID and IP ad
  private static SSncPws initPws(NL2Vpn l2Vpn, boolean hasProtect) {
    if (l2Vpn == null || l2Vpn.getPws() == null) {
      LOGGER.error("l2vpn or pwList is null.");
      return null;
    }

    SSncPws sSncPws = new SSncPws();
    Integer encaplateType = EncaplateTypeConvertor.NToS(l2Vpn.getEncapsulationType());
    Integer ctrlWord = CtrlWordConvertor.NToS(l2Vpn.getCtrlWordType());
    Integer adminStatus = AdminStatusConvertor.NToS(l2Vpn.getAdminStatus());
    Integer operateStatus = OperateStatusConvertor.NToS(l2Vpn.getOperStatus());

    Set<String> NePairSet = new HashSet<String>();
    List<NPw> nPws = l2Vpn.getPws().getPws();
    for (NPw nPw : nPws) {

    }
    return sSncPws;
  }

}
