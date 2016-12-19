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
import org.junit.Before;
import org.junit.Test;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.south.SQos;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;

/**
 * The UT class of SQosInitiator
 */
public class SQosInitiatorTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void test_Qos_On() throws Exception {
        SQos expected = getExpected("src/test/resource/json/qos/qos_on_output.json");
        SQos calculated = getCalculateResult("src/test/resource/json/qos/qos_on_input.json");
        Assert.assertEquals(expected, calculated);

    }

    private SQos getExpected(String expectedJson)
        throws Exception {
        File routeCase = new File(expectedJson);
        JsonParser routeParser = new JsonParser();
        JsonElement routeBody = routeParser.parse(new FileReader(routeCase));

        Type SQos = new TypeToken<SQos>() {}.getType();
        return gson.fromJson(routeBody, SQos);
    }

    private SQos getCalculateResult(String inputJson)
        throws Exception {
        File crtCase = new File(inputJson);
        JsonParser crtParser = new JsonParser();
        JsonElement crtBody = crtParser.parse(new FileReader(crtCase));

        Type parseType = new TypeToken<NMplsTePolicy>() {
        }.getType();
        NMplsTePolicy mplsTePolicy = gson.fromJson(crtBody, parseType);

        return SQosInitiator.initQos(mplsTePolicy);
    }

}