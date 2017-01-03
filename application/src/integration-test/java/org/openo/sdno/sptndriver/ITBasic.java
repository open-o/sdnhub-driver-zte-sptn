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
 *
 */

package org.openo.sdno.sptndriver;

import static org.mockserver.integration.ClientAndProxy.startClientAndProxy;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.core.MediaType;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.socket.PortFactory;
import org.openo.sdno.sptndriver.models.esr.SdnController;

/**
 * The basic IT class.
 */
abstract public class ITBasic {
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yaml");
    private static final int CONTROLLER_PORT = 21180;
    @ClassRule
    public static final DropwizardAppRule<SptnDriverConfig> RULE
        = new DropwizardAppRule<>(App.class, CONFIG_PATH);
    private static Flyway flyway;
    private static ClientAndProxy proxy;
    protected static ClientAndServer mockServer;

    @BeforeClass
    public static void createDb() {
        flyway = new Flyway();
        DataSourceFactory f = RULE.getConfiguration().getDataSourceFactory();
        flyway.setDataSource(f.getUrl(), f.getUser(), f.getPassword());
        flyway.migrate();
    }

    // when we're done, destroy the db
    @AfterClass
    public static void deleteDb() throws IOException {
        Files.deleteIfExists(Paths.get("test.h2.db"));
    }

    @BeforeClass
    public static void startProxy() {
        proxy = startClientAndProxy(PortFactory.findFreePort());
    }

    @AfterClass
    public static void stopProxy() {
        proxy.stop();
    }

    @Before
    public void startMockServer() {
        int msbPort = getPort(RULE.getConfiguration().getMsbUrl());
        mockServer = startClientAndServer(msbPort,CONTROLLER_PORT);
        proxy.reset();
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    private void mockEsr() {
        SdnController sdnController = new SdnController();
        sdnController.setSdnControllerId("controller1");
        sdnController.setUrl("http://localhost:21180");
        mockServer
            .when(
                request()
                    .withPath("/openoapi/extsys/v1/sdncontrollers/controller1")
            )
            .respond(
                response()
                    .withHeaders(
                        new Header(HttpHeaders.Names.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    )
                    .withBody((new Gson()).toJson(sdnController))
                    .withStatusCode(HttpStatusCode.OK_200.code())
            );
    }

    /**
     * Get port value from url like "http://localhost:10080/".
     * @param url Url like "http://localhost:10080/"
     * @return Port value like 10080
     */
    private static int getPort(String url) {
        String[] strArray = url.split(":");
        String[] strArray2 = strArray[2].split("/");
        return Integer.parseInt(strArray2[0]);
    }

    protected void initMockServer()
        throws Exception {
        mockEsr();
    }


}