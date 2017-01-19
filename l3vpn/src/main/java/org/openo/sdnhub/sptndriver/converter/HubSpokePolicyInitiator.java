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

import org.openo.sdnhub.sptndriver.models.north.NL3Ac;
import org.openo.sdnhub.sptndriver.exception.ParamErrorException;
import org.openo.sdnhub.sptndriver.models.north.NHubGroup;
import org.openo.sdnhub.sptndriver.models.north.NL3Acs;
import org.openo.sdnhub.sptndriver.models.north.NSpokeAcs;
import org.openo.sdnhub.sptndriver.models.north.NSpokeGroup;
import org.openo.sdnhub.sptndriver.models.north.NTopologyService;
import org.openo.sdnhub.sptndriver.models.south.SBelongedHubs;
import org.openo.sdnhub.sptndriver.models.south.SHubSpokeNode;
import org.openo.sdnhub.sptndriver.models.south.SHubSpokeNodes;
import org.openo.sdnhub.sptndriver.models.south.SHubSpokePolicy;
import org.openo.sdnhub.sptndriver.models.south.SL3vpn;
import org.openo.sdnhub.sptndriver.models.south.SNodeId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class to initiate hub spoke policy.
 */
public class HubSpokePolicyInitiator {

    private HubSpokePolicyInitiator(){}

    /**
     * The method to initiate hub spoke policy.
     *
     * @param topologyService NBI topology service.
     * @param topoModeEnum    SBI topology enumerator.
     * @param l3acs           NBI L3acs.
     * @return SBI hub spoke policy.
     */
    public static SHubSpokePolicy initHubSpokePolicy(NTopologyService topologyService,
                                                     NL3Acs l3acs,
                                                     SL3vpn.TopoModeEnum topoModeEnum)
        throws ParamErrorException {
        SHubSpokePolicy hubSpokePolicy = new SHubSpokePolicy();
        hubSpokePolicy.setHubFullmeshed(false);
        hubSpokePolicy.setPrimaryBackupConnected(true);
        if (topoModeEnum == SL3vpn.TopoModeEnum.HUB_SPOKE) {
            hubSpokePolicy.setNodeList(convertNbiToSbi(topologyService, l3acs));
        } else if (topoModeEnum == SL3vpn.TopoModeEnum.ANY_TO_ANY) {
            hubSpokePolicy.setHubFullmeshed(true);
        }
        return hubSpokePolicy;
    }

    private static SHubSpokeNodes convertNbiToSbi(NTopologyService topologyService,
                                                  NL3Acs l3acs)
        throws ParamErrorException {
        if (l3acs == null || l3acs.getAc() == null) {
            throw new ParamErrorException("Input l3 ac list is null.");
        }
        if (topologyService == null) {
            throw new ParamErrorException("Input topology service is null.");
        }
        Map<String, String> acIdToNeIdMap = getAcIdToNeIdMap(l3acs);
        SHubSpokeNodes hubSpokeNodes = new SHubSpokeNodes();

        List<SNodeId> nodeList = getNodeList(topologyService.getHubGroups(),
            acIdToNeIdMap);
        hubSpokeNodes.getHubSpokeNodes()
            .addAll(convertSpokeNode(topologyService, acIdToNeIdMap, nodeList));
        hubSpokeNodes.getHubSpokeNodes()
            .addAll(convertHubNode(topologyService, acIdToNeIdMap));

        return hubSpokeNodes;
    }

    private static Map<String, String> getAcIdToNeIdMap(NL3Acs l3acs)
        throws ParamErrorException {
        if (l3acs == null || l3acs.getAc() == null) {
            throw new ParamErrorException("Input l3 ac list is null.");
        }
        List<NL3Ac> acList = l3acs.getAc();
        Map<String, String> acIdToNeIdMap = new HashMap<>();
        for (NL3Ac l3ac : acList) {
            acIdToNeIdMap.put(l3ac.getId(), l3ac.getNeId());
        }
        return acIdToNeIdMap;
    }

    private static List<SNodeId> getNodeList(List<NHubGroup> hubGroups,
                                             Map<String, String> acIdToNeIdMap) {
        List<SNodeId> nodeList = new ArrayList<>();
        for (NHubGroup hubGroup : hubGroups) {
            SNodeId nodeId = new SNodeId();
            nodeId.setNodeId(acIdToNeIdMap.get(hubGroup.getAcId()));
            nodeList.add(nodeId);
        }
        return nodeList;
    }

    private static List<SHubSpokeNode> convertHubNode(NTopologyService topologyService,
                                                      Map<String, String> acIdToNeIdMap) {
        List<SHubSpokeNode> hubSpokeNodeList = new ArrayList<>();
        List<NHubGroup> hubGroups = topologyService.getHubGroups();
        for (NHubGroup hubGroup : hubGroups) {
            SHubSpokeNode hubNode = new SHubSpokeNode();
            hubNode.setNeId(acIdToNeIdMap.get(hubGroup.getAcId()));
            hubNode.setNodeRole(SHubSpokeNode.NodeRoleEnum.HUB);
            hubSpokeNodeList.add(hubNode);
        }
        return hubSpokeNodeList;
    }

    private static List<SHubSpokeNode> convertSpokeNode(NTopologyService topologyService,
                                                        Map<String, String> acIdToNeIdMap,
                                                        List<SNodeId> nodeList) {
        List<SHubSpokeNode> hubSpokeNodes = new ArrayList<>();
        NSpokeGroup spokeGroup = topologyService.getSpokeGroup();
        if (spokeGroup != null) {
            List<NSpokeAcs> spokeAcList = spokeGroup.getSpokeAc();
            for (NSpokeAcs spokeAcs : spokeAcList) {
                SHubSpokeNode spokeNode = new SHubSpokeNode();
                spokeNode.setNeId(acIdToNeIdMap.get(spokeAcs.getAcId()));
                spokeNode.setNodeRole(SHubSpokeNode.NodeRoleEnum.SPOKE);
                SBelongedHubs sBelongedHubs = new SBelongedHubs();
                sBelongedHubs.setBelongedHubList(nodeList);
                spokeNode.setBelongedHubs(sBelongedHubs);
                hubSpokeNodes.add(spokeNode);
            }
        }
        return hubSpokeNodes;
    }
}
