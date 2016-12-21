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


import org.junit.Assert;
import org.junit.Test;
import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;
import org.openo.sdno.sptndriver.utils.JsonUtil;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * The class to test SRouteCalReqsInitiator.
 */
public class SRouteCalReqsInitiatorTest {

    @Test
    public void initElineLspCalRoute() throws Exception {
        SRouteCalReqsInput calculated
            = getCalculateResult("src/test/resource/json/cal_tunnel_route_input.json");
        SRouteCalReqsInput expected
            = getExpected("src/test/resource/json/cal_tunnel_route_output.json");
        Assert.assertEquals(expected, calculated);
    }

    @Test
    public void test_particular_constraint_not_empty() throws Exception {
        SRouteCalReqsInput calculated
            = getCalculateResult("src/test/resource/json/routecalreqs/particular_constraint_not_empty_input.json");
        SRouteCalReqsInput expected
            = getExpected("src/test/resource/json/routecalreqs/particular_constraint_not_empty_output.json");
        Assert.assertEquals(expected, calculated);
    }

    @Test
    public void test_ne_empty() throws Exception {
        try {
            getCalculateResult("src/test/resource/json/routecalreqs/ne_empty_input.json");
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("Ingress ne or egress ne is null or empty."));
            return;
        }
        fail("Expect ParamErrorException for empty ne.");
    }

    @Test
    public void test_use_pw_ne() throws Exception {
        SRouteCalReqsInput calculated
            = getCalculateResult("src/test/resource/json/routecalreqs/ac_empty_pw_not_empty_input.json");
        SRouteCalReqsInput expected
            = getExpected("src/test/resource/json/routecalreqs/ac_empty_pw_not_empty_output.json");
        Assert.assertEquals(expected, calculated);
    }

    @Test
    public void test_input_l2vpn_null() {
        try {
            SRouteCalReqsInitiator.initElineLspCalRoute(null);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("Input l2vpn is null."));
            return;
        }
        fail("Expect ParamErrorException for null l2vpn.");
    }

    @Test
    public void test_no_protection_and_not_best_effort() throws Exception {
        SRouteCalReqsInput calculated
            = getCalculateResult("src/test/resource/json/routecalreqs/no_protection_not_besteffort_input.json");
        SRouteCalReqsInput expected
            = getExpected("src/test/resource/json/routecalreqs/no_protection_not_besteffort_output.json");
        Assert.assertEquals(expected, calculated);
    }

    private SRouteCalReqsInput getExpected(String jsonFileName)
        throws Exception {
        return JsonUtil.parseJsonFromFile(jsonFileName, SRouteCalReqsInput.class);
    }

    private SRouteCalReqsInput getCalculateResult(String jsonFileName)
        throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(jsonFileName, NL2Vpn.class);
        return SRouteCalReqsInitiator.initElineLspCalRoute(l2vpn);
    }
}
