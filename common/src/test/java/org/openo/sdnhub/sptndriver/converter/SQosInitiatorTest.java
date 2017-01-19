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

import org.junit.Assert;
import org.junit.Test;
import org.openo.sdnhub.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdnhub.sptndriver.models.south.SQos;
import org.openo.sdnhub.sptndriver.utils.JsonUtil;

/**
 * The UT class of SQosInitiator
 */
public class SQosInitiatorTest {

    @Test
    public void test_Qos_On() throws Exception {
        SQos expected = getExpected("src/test/resources/json/qos/qos_on_output.json");
        SQos calculated = getCalculateResult("src/test/resources/json/qos/qos_on_input.json");
        Assert.assertEquals(expected, calculated);

    }

    private SQos getExpected(String jsonFileName)
        throws Exception {
        return JsonUtil.parseJsonFromFile(jsonFileName, SQos.class);
    }

    private SQos getCalculateResult(String jsonFileName)
        throws Exception {
        NMplsTePolicy mplsTePolicy = JsonUtil.parseJsonFromFile(jsonFileName, NMplsTePolicy.class);
        return SQosInitiator.initQos(mplsTePolicy);
    }

}