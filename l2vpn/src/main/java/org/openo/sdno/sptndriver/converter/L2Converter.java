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

import org.openo.sdno.sptndriver.enums.AdminStatusEnum;
import org.openo.sdno.sptndriver.enums.OperateStatusEnum;
import org.openo.sdno.sptndriver.enums.ac.AccessActionEnum;
import org.openo.sdno.sptndriver.enums.ac.AccessTypeEnum;
import org.openo.sdno.sptndriver.enums.pw.CtrlWordEnum;
import org.openo.sdno.sptndriver.enums.pw.EncapsulateTypeEnum;
import org.openo.sdno.sptndriver.enums.south.pw.SSnSupport;
import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Ac;
import org.openo.sdno.sptndriver.models.north.NL2Access;
import org.openo.sdno.sptndriver.models.north.NL2Acs;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NPw;
import org.openo.sdno.sptndriver.models.north.NPws;
import org.openo.sdno.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SEgressEps;
import org.openo.sdno.sptndriver.models.south.SEline;
import org.openo.sdno.sptndriver.models.south.SIngressEps;
import org.openo.sdno.sptndriver.models.south.SNcdResourceRelation;
import org.openo.sdno.sptndriver.models.south.SServiceEndPoint;
import org.openo.sdno.sptndriver.models.south.SSncPw;
import org.openo.sdno.sptndriver.models.south.SSncPws;
import org.openo.sdno.sptndriver.models.south.SSncType;
import org.openo.sdno.sptndriver.utils.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The class to convert from L2vpn to E-Line.
 */
public class L2Converter {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(L2Converter.class);

    private L2Converter() {
    }

    /**
     * Convert create L2vpn to E-Line.
     *
     * @param l2vpn L2vpn creating parameters in NBI.
     * @return E-Line and tunnel creating parameters in SBI.
     */
    public static SCreateElineAndTunnelsInput convertL2ToElineTunnerCreator(NL2Vpn l2vpn)
        throws ParamErrorException {
        SCreateElineAndTunnels createElineAndTunnels = new SCreateElineAndTunnels();
        createElineAndTunnels.setSncEline(l2ToEline(l2vpn, null));
        createElineAndTunnels.setSncTunnelCreatePolicy(SSncTunnelCreatePolicyInitiator
            .initTunnelPolicy(l2vpn.getTunnelService()));
        SCreateElineAndTunnelsInput createElineAndTunnelsInput = new SCreateElineAndTunnelsInput();
        createElineAndTunnelsInput.setInput(createElineAndTunnels);
        return createElineAndTunnelsInput;
    }

    /**
     * convert L2vpn to E-Line.
     *
     * @param nl2Vpn       NBI L2vpn.
     * @param southElineId E-Line UUID.
     * @return SBI E-Line.
     */
    private static SEline l2ToEline(NL2Vpn nl2Vpn, String southElineId)
        throws ParamErrorException {
        if (nl2Vpn == null) {
            throw new ParamErrorException("Input l2vpn is null.");
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
     * Convert ingress or egress AC list, only support 2 ACs.
     *
     * @param acList    NBI AC list.
     * @param isIngress Whether is ingress or egress AC.
     * @return SBI AC list, if input NBI AC list size is not 2, return null.
     */
    private static SServiceEndPoint northToSouth(NL2Acs acList, boolean isIngress)
        throws ParamErrorException {
        if (acList == null
            || acList.getAc() == null
            || acList.getAc().size() != 2) {
            throw new ParamErrorException("Input acList size must be 2.");
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
    private static SServiceEndPoint northToSouth(NL2Ac ac)
        throws ParamErrorException {
        SServiceEndPoint ep = new SServiceEndPoint();
        ep.setId(ac.getId());
        ep.setNeId(ac.getNeId());
        ep.setLtpId(ac.getLtpId());

        NL2Access nl2Access = ac.getL2Access();
        if (nl2Access != null) {
            ep.setAccessType(AccessTypeEnum.convertNbiToSbi(nl2Access.getAccessType().toString()));
            if (nl2Access.getDot1qVlanBitmap() != null) {
                ep.setDot1qVlanBitmap(nl2Access.getDot1qVlanBitmap().toString());
            }
            if (nl2Access.getQinqCvlanBitmap() != null) {
                ep.setQinqCvlanBitmap(nl2Access.getQinqCvlanBitmap().toString());
            }
            if (nl2Access.getQinqSvlanBitmap() != null) {
                ep.setQinqSvlanBitmap(nl2Access.getQinqSvlanBitmap().toString());
            }
            String accessAction = nl2Access.getAccessAction().toString();
            ep.setAccessAction(AccessActionEnum.convertNbiToSbi(accessAction));
            if (accessAction != null) {
                if (accessAction.equals(AccessActionEnum.PUSH.getNorth())) {
                    ep.setActionVlanId(nl2Access.getPushVlanId().toString());
                } else if (accessAction.equals(AccessActionEnum.SWAP.getNorth())) {
                    ep.setActionVlanId(nl2Access.getSwapVlanId().toString());
                }
            }

            ep.setQos(SQosInitiator
                .initAcQos(ep.getId(), ac.getUpstreamBandwidth(), ac.getDownstreamBandwidth()));
        }

        ep.setRole(SServiceEndPoint.RoleEnum.MASTER);

        return ep;
    }

    /**
     * Whether has backup PW(Pseudo Wire).
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
            if (pw.getProtectionRole() != null
                && pw.getProtectionRole().equals(NPw.ProtectionRoleEnum.BACKUP)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initialize PW(Pseudo Wire) from NBI L2vpn, PW protection is not supported now, so there is
     * only 2 NEs, NE_A and NE_Z and PW role is always MASTER.
     *
     * @param l2Vpn NBI L2vpn.
     * @return SBI PW list.
     */
    private static SSncPws initPws(NL2Vpn l2Vpn) throws ParamErrorException {
        if (l2Vpn == null
            || l2Vpn.getPws() == null
            || l2Vpn.getPws().getPws().size() != 2) {
            throw new ParamErrorException("L2vpn should includes 2 pws.");
        }

        SSncPws pwList = new SSncPws();

        NPw pwA = l2Vpn.getPws().getPws().get(0);
        NPw pwZ = l2Vpn.getPws().getPws().get(1);

        SSncPw sncPw = new SSncPw();
        sncPw.setId(UuidUtil.getUuid());
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
        // Doesn't support protect now, no need to set oam.
        sncPw.setOam(null);
        pwList.setSncPw(sncPw);

        return pwList;
    }

    /**
     * Parse resource id from response.
     *
     * @param cmdOutput Response from controller.
     * @return Resource id.
     */
    public static String getReturnId(SCmdResultAndNcdResRelationsOutput cmdOutput) {
        if (cmdOutput != null
            && cmdOutput.getOutput() != null
            && cmdOutput.getOutput().getNcdResourceRelations() != null
            && cmdOutput.getOutput().getNcdResourceRelations().getNcdResourceRelation() != null) {
            List ncdResourceRelation
                = cmdOutput.getOutput().getNcdResourceRelations().getNcdResourceRelation();
            if (!ncdResourceRelation.isEmpty()) {
                return ((SNcdResourceRelation) ncdResourceRelation.get(0)).getResourceId();
            }
        }
        return null;
    }

}
