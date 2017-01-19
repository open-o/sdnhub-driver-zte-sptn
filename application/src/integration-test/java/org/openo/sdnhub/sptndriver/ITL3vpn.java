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

package org.openo.sdnhub.sptndriver;

import org.junit.Test;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.model.JsonBody;
import org.openo.sdnhub.sptndriver.models.north.NCreateL3vpnReq;
import org.openo.sdnhub.sptndriver.utils.FileUtil;
import org.openo.sdnhub.sptndriver.utils.JsonUtil;

import java.io.IOException;

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
 * The It class of L3vpn.
 */
public class ITL3vpn extends ITBasic {

    @Test
    public void testCreateAndDelete()
        throws Exception {
        initMockServer();
        Client client = ClientBuilder.newClient();
        String l3vpnUuid = createL3vpn(client);
        deleteL3vpn(client, l3vpnUuid);
    }

    private String createL3vpn(Client client)
        throws Exception {
        NCreateL3vpnReq createL3vpnReq
            = JsonUtil.parseJsonFromFile(
            ResourceHelpers.resourceFilePath("json/l3vpn/input.json"), NCreateL3vpnReq.class);
        Response response = client.target(getCreateUrl())
            .request().header("X-Driver-Parameter", "extSysID=controller1")
            .post(Entity.entity(createL3vpnReq, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(HttpStatusCode.CREATED_201.code(), response.getStatus());
        return createL3vpnReq.getL3vpn().getId();
    }

    private void deleteL3vpn(Client client, String l3vpnUuid)
        throws Exception {
        Response response = client.target(getDeleteUrl(l3vpnUuid))
            .request().header("X-Driver-Parameter", "extSysID=controller1")
            .delete();
        assertEquals(HttpStatusCode.OK_200.code(), response.getStatus());
    }

    private String getCreateUrl() {
        return "http://localhost:" + RULE.getLocalPort()
            + "/openoapi/sbi-l3vpn/v1/l3vpns";
    }

    private String getDeleteUrl(String l3vpnUuid) {
        return getCreateUrl() + "/" + l3vpnUuid;
    }

    protected void initMockServer()
        throws Exception {
        super.initMockServer();
        mockCreateL3vpn();
        mockDeleteL3vpn();
    }

    private void mockCreateL3vpn() throws IOException {
        String requestBody = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l3vpn/controller_create_l3vpn_request.json"));
        String responseBody = FileUtil.readFile(
            ResourceHelpers.resourceFilePath("json/l3vpn/controller_create_l3vpn_response.json"));
        mockServer
            .when(
                request()
                    .withPath("/restconf/data/sptn-service-l3vpn:service/snc-l3vpns")
                    .withBody(new JsonBody(requestBody, MatchType.ONLY_MATCHING_FIELDS))
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

    private void mockDeleteL3vpn() {
        mockServer
            .when(
                request()
                    .withPath(
                        "/restconf/data/sptn-service-l3vpn:service/snc-l3vpns/snc-l3vpn=668d75c3-dc21-45a4-8e0b-f2d49b617888")
                    .withMethod("DELETE")
            )
            .respond(
                response()
                    .withStatusCode(HttpStatusCode.NO_CONTENT_204.code())
            );
    }
}
