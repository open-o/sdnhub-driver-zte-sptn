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
import org.openo.sdnhub.sptndriver.models.south.SSncSwitch;
import org.openo.sdnhub.sptndriver.utils.JsonUtil;

/**
 * The UT class of SSncSwitchInitiator.
 */
public class SSncSwitchInitiatorTest {

    @Test
    public void test_pw_sncswitch() throws Exception {
        SSncSwitch expectedValue = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/pw_output.json", SSncSwitch.class);
        SSncSwitch actualValue = SSncSwitchInitiator.initPwSncSwitch("pw1", true);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void test_not_revertive() throws Exception {
        SSncSwitch expectedValue = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/nonrevertive_output.json", SSncSwitch.class);
        NMplsTePolicy mplsTePolicy = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/nonrevertive_input.json", NMplsTePolicy.class);
        SSncSwitch actualValue = SSncSwitchInitiator.initLspSncSwitch(mplsTePolicy);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void test_unprotected() throws Exception {
        SSncSwitch expectedValue = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/unprotected_output.json", SSncSwitch.class);
        SSncSwitch actualValue = SSncSwitchInitiator.initLspSncSwitch(null);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void test_with_recovery_1_to_1() throws Exception {
        SSncSwitch expectedValue = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/coroute_output.json", SSncSwitch.class);
        NMplsTePolicy mplsTePolicy = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/sncswitch/coroute_input.json", NMplsTePolicy.class);
        SSncSwitch actualValue = SSncSwitchInitiator.initLspSncSwitch(mplsTePolicy);
        Assert.assertEquals(expectedValue, actualValue);
    }
}