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


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Test;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;

/**
 * The class to test SRouteCalReqsInitiator.
 *
 */
public class SRouteCalReqsInitiatorTest
{
    @Test
    public void initElineLspCalRoute() throws Exception
    {

        File crtCase = new File("src/test/resource/json/cal_tunnel_route_input.json");
        JsonParser crtParser = new JsonParser();
        JsonElement crtBody = crtParser.parse(new FileReader(crtCase));

        Gson l2Gson = new Gson();
        Type l2vpnType = new TypeToken<NL2Vpn>(){}.getType();
        NL2Vpn l2vpn = l2Gson.fromJson(crtBody, l2vpnType);

        SRouteCalReqsInput routeCalInput = SRouteCalReqsInitiator.initElineLspCalRoute(l2vpn);

        File routeCase = new File("src/test/resource/json/cal_tunnel_route_output.json");
        JsonParser routeParser = new JsonParser();
        JsonElement routeBody = routeParser.parse(new FileReader(routeCase));

        Gson routeGson = new Gson();
        Type SRouteCalReqsInput = new TypeToken<SRouteCalReqsInput>(){}.getType();
        SRouteCalReqsInput calculateExpectation = routeGson.fromJson(routeBody, SRouteCalReqsInput);

        Assert.assertEquals(routeCalInput, calculateExpectation);
    }



}
