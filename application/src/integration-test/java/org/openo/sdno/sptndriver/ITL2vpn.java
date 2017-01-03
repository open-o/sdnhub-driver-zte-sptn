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

package org.openo.sdno.sptndriver;

import org.junit.Test;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.JsonBody;
import org.openo.sdno.sptndriver.models.north.NCreateL2vpnReq;
import org.openo.sdno.sptndriver.utils.FileUtil;
import org.openo.sdno.sptndriver.utils.JsonUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.testing.ResourceHelpers;
import io.netty.handler.codec.http.HttpHeaders;

import static org.junit.Assert.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * The It class of L2vpn.
 */
public class ITL2vpn extends ITBasic {
    @Test
    public void testCreateAndDelete()
        throws Exception {
        initMockServer();
        Client client = ClientBuilder.newClient();
        String l2vpnUuid = createL2vpn(client);
        deleteL2vpn(client, l2vpnUuid);
    }

    private String createL2vpn(Client client) throws Exception {
        NCreateL2vpnReq createL2vpnReq
            = JsonUtil.parseJsonFromFile(
            ResourceHelpers.resourceFilePath("json/l2vpn/create_request.json"), NCreateL2vpnReq.class);
        Response response = client.target(getCreateUrl())
            .request().header("X-Driver-Parameter", "extSysID=controller1")
            .post(Entity.entity(createL2vpnReq, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(HttpStatusCode.CREATED_201.code(), response.getStatus());
        return createL2vpnReq.getL2vpnVpws().getId();
    }

    private void deleteL2vpn(Client client, String l2vpnUuid) {
        Response response = client.target(getDeleteUrl(l2vpnUuid))
            .request().header("X-Driver-Parameter", "extSysID=controller1")
            .delete();
        assertEquals(HttpStatusCode.OK_200.code(), response.getStatus());
    }

    private String getCreateUrl() {
        return "http://localhost:" + RULE.getLocalPort()
            + "/openoapi/sbi-l2vpn-vpws/v1/l2vpn_vpwss";
    }

    private String getDeleteUrl(String l2vpnUuid) {
        return getCreateUrl() + "/" + l2vpnUuid;
    }

    protected void initMockServer()
        throws Exception {
        super.initMockServer();
        mockCalculateTunnelRoute();
        mockCreateL2vpn();
        mockDeleteL2vpn();
    }

    private void mockCalculateTunnelRoute()
        throws Exception {
        String inputJson = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l2vpn/controller/calculate_tunnel_route_request.json"));
        String responseBody = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l2vpn/controller/calculate_tunnel_route_response.json"));
        mockServer
            .when(
                request()
                    .withPath("/restconf/operations/sptn-service-route:request-routes")
                    .withBody(new JsonBody(inputJson))
            )
            .respond(
                response()
                    .withHeaders(
                        new Header(HttpHeaders.Names.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    )
                    .withBody(responseBody)
                    .withStatusCode(HttpStatusCode.OK_200.code())
            );
    }

    private void mockCreateL2vpn() throws Exception {
        String inputJson = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l2vpn/controller/create_eline_request.json"));
        String responseBody = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l2vpn/controller/create_eline_response.json"));
        mockServer
            .when(
                request()
                    .withPath("/restconf/operations/sptn-service-eline:create-snc-eline-tunnels")
                    .withBody(new JsonBody(inputJson, MatchType.ONLY_MATCHING_FIELDS))
            )
            .respond(
                response()
                    .withHeaders(
                        new Header(HttpHeaders.Names.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    )
                    .withBody(responseBody)
                    .withStatusCode(HttpStatusCode.CREATED_201.code())
            );
    }

    private void mockDeleteL2vpn() {
        mockServer
            .when(
                request()
                    .withPath("/restconf/operations/sptn-service-eline:delete-snc-eline")
            )
            .respond(
                response()
                    .withStatusCode(HttpStatusCode.NO_CONTENT_204.code())
            );
    }
}
