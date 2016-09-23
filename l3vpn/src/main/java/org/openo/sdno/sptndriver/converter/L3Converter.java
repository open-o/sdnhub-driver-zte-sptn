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
import org.openo.sdno.sptndriver.enums.l3vpn.RouteProtocolEnum;
import org.openo.sdno.sptndriver.enums.l3vpn.TopoModeEnum;
import org.openo.sdno.sptndriver.models.north.NL3Ac;
import org.openo.sdno.sptndriver.models.north.NL3Acs;
import org.openo.sdno.sptndriver.models.north.NL3Vpn;
import org.openo.sdno.sptndriver.models.north.NRoute;
import org.openo.sdno.sptndriver.models.north.NRoutes;
import org.openo.sdno.sptndriver.models.north.NStaticRoute;
import org.openo.sdno.sptndriver.models.south.SL3ac;
import org.openo.sdno.sptndriver.models.south.SL3acProtocol;
import org.openo.sdno.sptndriver.models.south.SL3vpn;
import org.openo.sdno.sptndriver.models.south.SStaticRoute;
import org.openo.sdno.sptndriver.utils.Ipv4Util;
import org.openo.sdno.sptndriver.utils.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The class to convert from NBI L3vpn to SBI L3vpn.
 */
public class L3Converter {
  private static final Logger LOGGER = LoggerFactory.getLogger(L3Converter.class);

  /**
   * Convert NBI L3vpn to SBI L3vpn.
   *
   * @param nl3Vpn NBI L3vpn.
   * @return SBI L3vpn.
   */
  public static SL3vpn convertNbiToSbi(NL3Vpn nl3Vpn) {
    if (nl3Vpn == null) {
      LOGGER.error("Input l3vpn is null.");
      return null;
    }
    SL3vpn sl3vpn = new SL3vpn();
    sl3vpn.setId(nl3Vpn.getId());
    sl3vpn.setName(null);
    sl3vpn.setUserLabel(nl3Vpn.getName());
    sl3vpn.setParentNcdId(null);
    sl3vpn.setAdminStatus(AdminStatusEnum.convertNbiToSbi(nl3Vpn.getAdminStatus()));
    sl3vpn.setOperateStatus(OperateStatusEnum.convertNbiToSbi(nl3Vpn.getOperStatus()));
    sl3vpn.setAcList(convertNbiToSbi(nl3Vpn.getAcs()));
    sl3vpn.setTopoMode(TopoModeEnum.convertNbiToSbi(nl3Vpn.getTopology()));
    sl3vpn.setHubSpokePolicy(HubSpokePolicyInitiator.initHubSpokePolicy(nl3Vpn.getTopologyService(),
        nl3Vpn.getAcs(), sl3vpn.getTopoMode()));
    sl3vpn.setBindRelationList(null);
    sl3vpn.setTunnelCreatePolicy(
        SSncTunnelCreatePolicyInitiator.initTunnelPolicy(nl3Vpn.getTunnelService()));
    // Maybe use protection group in NBI and topology type in the future to determine the frr list.
    sl3vpn.setL3FrrList(null);
    sl3vpn.setStaticRouteList(null);
    if (nl3Vpn.getDiffServ() != null) {
      sl3vpn.setTrafficClass(
          TrafficClassConverter.getEnum(nl3Vpn.getDiffServ().getServiceClass().toString()));
    }

    return sl3vpn;
  }

  private static List<SL3ac> convertNbiToSbi(NL3Acs nl3Acs) {
    List<SL3ac> sl3acs = new ArrayList<>();
    for (NL3Ac nl3Ac : nl3Acs.getAc()) {
      sl3acs.add(convertAc(nl3Ac));
    }
    return sl3acs;
  }

  private static SL3ac convertAc(NL3Ac nl3Ac) {
    if (nl3Ac == null) {
      LOGGER.error("North L3ac is null.");
      return null;
    }

    SL3ac sl3ac = new SL3ac();
    sl3ac.setId(nl3Ac.getId());
    sl3ac.setNeId(nl3Ac.getNeId());
    sl3ac.setLtpId(nl3Ac.getLtpId());
    if (nl3Ac.getL3Access() != null) {
      sl3ac.setIpAddr(nl3Ac.getL3Access().getIpv4Address());
      sl3ac.setProtocolList(convertProtocol(nl3Ac.getL3Access().getRoutes(), sl3ac.getLtpId()));
    }
    sl3ac.setQos(SQosInitiator.initAcQos(nl3Ac.getId(),
        nl3Ac.getUpstreamBandwidth(), nl3Ac.getDownstreamBandwidth()));

    return sl3ac;
  }

  private static List<SL3acProtocol> convertProtocol(NRoutes routes, String ltpId) {
    if (routes == null) {
      LOGGER.error("North routes is null.");
      return null;
    }

    List<SL3acProtocol> sl3acProtocols = new ArrayList<>();
    for (NRoute route : routes.getRoute()) {
      SL3acProtocol sl3acProtocol = convertProtocol(route, ltpId);
      sl3acProtocols.add(sl3acProtocol);
    }

    return sl3acProtocols;
  }

  private static SL3acProtocol convertProtocol(NRoute route, String ltpId) {
    if (route == null) {
      LOGGER.error("North route is null.");
      return null;
    }
    SL3acProtocol sl3acProtocol = new SL3acProtocol();
    sl3acProtocol.setProtocolType(RouteProtocolEnum.convertNbiToSbi(route.getType()));
    if (sl3acProtocol.getProtocolType()
        .equals(RouteProtocolEnum.STATIC.getSouthValue())
        && route.getStaticRoutes() != null
        && route.getStaticRoutes().getStaticRoute() != null) {
      List<SStaticRoute> southStaticRoutes = new ArrayList<>();
      List<NStaticRoute> northStaticRoutes = route.getStaticRoutes().getStaticRoute();
      for (NStaticRoute northStaticRoute : northStaticRoutes) {
        southStaticRoutes.add(convertStaticRoute(northStaticRoute, ltpId));
      }
      sl3acProtocol.setStaticRoutes(southStaticRoutes);
    }
    return sl3acProtocol;
  }

  private static SStaticRoute convertStaticRoute(NStaticRoute northStaticRoute,
                                                 String ltpId) {
    SStaticRoute southStaticRoute = new SStaticRoute();
    southStaticRoute.setId(UuidUtil.getUuid());
    southStaticRoute.setDestIp(Ipv4Util.getDotIp(northStaticRoute.getIpPrefix()));
    southStaticRoute.setDestMask(Ipv4Util.getDotMask(northStaticRoute.getIpPrefix()));
    southStaticRoute.setRouteType(SStaticRoute.RouteTypeEnum.LOCAL_ROUTE);
    southStaticRoute.setNextHopIp(northStaticRoute.getNextHop());
    southStaticRoute.setOutInf(ltpId);
    southStaticRoute.setRouteWeight("1");
    southStaticRoute.setRouteStatus(SStaticRoute.RouteStatusEnum.UP);
    return southStaticRoute;
  }

}
