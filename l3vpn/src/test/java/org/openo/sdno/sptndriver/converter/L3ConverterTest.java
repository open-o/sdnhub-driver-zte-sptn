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
import org.openo.sdno.sptndriver.models.north.NL3Vpn;
import org.openo.sdno.sptndriver.models.south.SL3BindRelation;
import org.openo.sdno.sptndriver.models.south.SL3Frr;
import org.openo.sdno.sptndriver.models.south.SL3ac;
import org.openo.sdno.sptndriver.models.south.SL3acProtocol;
import org.openo.sdno.sptndriver.models.south.SL3vpn;
import org.openo.sdno.sptndriver.models.south.SStaticRoute;
import org.openo.sdno.sptndriver.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The class to test L3Convert.
 */
public class L3ConverterTest {
    @Test
    public void testFullMesh() throws Exception {
       compareInputAndOutput("src/test/resource/json/create_l3vpn_input.json",
           "src/test/resource/json/create_l3vpn_output.json");
    }

    @Test
    public void testHubSpoke() throws Exception {
        compareInputAndOutput("src/test/resource/json/hub_spoke_policy_input.json",
            "src/test/resource/json/hub_spoke_policy_output.json");
    }

    @Test
    public void testAcQos() throws Exception {
        compareInputAndOutput("src/test/resource/json/ac_qos_input.json",
            "src/test/resource/json/ac_qos_output.json");
    }

    private void compareInputAndOutput (String inputJsonFile, String outputJsonFile)
        throws Exception{
        SL3vpn calculatedOutput = getCalculateResult(inputJsonFile);
        SL3vpn expectedOutput = getExpected(outputJsonFile);

        clearStaticRouteId(calculatedOutput);
        clearStaticRouteId(expectedOutput);
        if (calculatedOutput.getL3FrrList() == null) {
            calculatedOutput.setL3FrrList(new ArrayList<SL3Frr>());
        }
        if (calculatedOutput.getBindRelationList() == null) {
            calculatedOutput.setBindRelationList(new ArrayList<SL3BindRelation>());
        }
        Assert.assertEquals(expectedOutput, calculatedOutput);
    }

    private void clearStaticRouteId(SL3vpn sl3vpn) {
        if (sl3vpn != null
            && sl3vpn.getAcs() != null) {
            List<SL3ac> sl3acList = sl3vpn.getAcs().getL3Acs();
            for (SL3ac l3ac : sl3acList) {
                if (l3ac.getProtocolList() != null) {
                    for (SL3acProtocol sl3acProtocol : l3ac.getProtocolList().getProtocols()) {
                        if (sl3acProtocol.getStaticRoutes() != null) {
                            for (SStaticRoute staticRoute : sl3acProtocol.getStaticRoutes().getStaticRouteList()) {
                                staticRoute.setId(null);
                            }
                        }
                    }
                }
            }
        }
    }

    private SL3vpn getExpected(String jsonFileName)
        throws Exception {
        return JsonUtil.parseJsonFromFile(jsonFileName, SL3vpn.class);
    }

    private SL3vpn getCalculateResult(String jsonFileName)
        throws Exception {
        NL3Vpn l3vpn = JsonUtil.parseJsonFromFile(jsonFileName, NL3Vpn.class);
        return L3Converter.convertNbiToSbi(l3vpn);
    }

}