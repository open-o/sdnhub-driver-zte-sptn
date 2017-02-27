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
import org.openo.sdnhub.sptndriver.exception.ParamErrorException;
import org.openo.sdnhub.sptndriver.models.north.NL2Vpn;
import org.openo.sdnhub.sptndriver.models.south.SCmdResultAndNcdResRelationsOutput;
import org.openo.sdnhub.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdnhub.sptndriver.models.south.SSncPw;
import org.openo.sdnhub.sptndriver.utils.JsonUtil;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * The class to test L2Convert
 */
public class L2ConverterTest {
    @Test
    public void convertL2ToElineTunnerCreator() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/create_l2vpn_input.json", NL2Vpn.class);
        SCreateElineAndTunnelsInput calculatedOutput = L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        SCreateElineAndTunnelsInput expectedOutput = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/create_l2vpn_output.json", SCreateElineAndTunnelsInput.class);

        // uuid of pw is different every time, so must clear the uuid.
        clearPwId(calculatedOutput);
        clearPwId(expectedOutput);
        Assert.assertEquals(expectedOutput,calculatedOutput);
    }

    @Test
    public void testNullInput() throws Exception {
        try {
            L2Converter.convertL2ToElineTunnerCreator(null);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("Input l2vpn is null."));
            return;
        }
        fail("Expect ParamErrorException for null input.");
    }

    @Test
    public void testAcSize() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/only_1_ac_input.json", NL2Vpn.class);
        try {
            L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("Input acList size must be 2."));
            return;
        }
        fail("Expect ParamErrorException for only 1 ac.");
    }

    @Test
    public void testNullPw() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/no_pw_input.json", NL2Vpn.class);
        try {
            L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("L2vpn should includes 2 pws."));
            return;
        }
        fail("Expect ParamErrorException for empty pw list.");
    }

    @Test
    public void testPwSize() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/only_1_pw_input.json", NL2Vpn.class);
        try {
            L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("L2vpn should includes 2 pws."));
            return;
        }
        fail("Expect ParamErrorException for only 1 pw.");
    }

    @Test
    public void testSwapAccessAction() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/accessaction_swap_input.json", NL2Vpn.class);
        SCreateElineAndTunnelsInput calculatedOutput = L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        SCreateElineAndTunnelsInput expectedOutput = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/accessaction_swap_output.json", SCreateElineAndTunnelsInput.class);

        // uuid of pw is different every time, so must clear the uuid.
        clearPwId(calculatedOutput);
        clearPwId(expectedOutput);
        Assert.assertEquals(expectedOutput, calculatedOutput);
    }

    @Test
    public void testBackupPw() throws Exception {
        NL2Vpn l2vpn = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/backup_pw_input.json", NL2Vpn.class);
        SCreateElineAndTunnelsInput calculatedOutput = L2Converter.convertL2ToElineTunnerCreator(l2vpn);
        SCreateElineAndTunnelsInput expectedOutput = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/backup_pw_output.json", SCreateElineAndTunnelsInput.class);

        // uuid of pw is different every time, so must clear the uuid.
        clearPwId(calculatedOutput);
        clearPwId(expectedOutput);
        Assert.assertEquals(expectedOutput, calculatedOutput);
    }

    @Test
    public void testGetReturnId() throws Exception {
        SCmdResultAndNcdResRelationsOutput cmdResultAndNcdResRelationsOutput  = JsonUtil.parseJsonFromFile(
            "src/test/resources/json/createl2/returnid_input.json", SCmdResultAndNcdResRelationsOutput.class);
        String calculated = L2Converter.getReturnId(cmdResultAndNcdResRelationsOutput);
        String expected = "53319217-4670-49c3-8d82-68ba1350c542";
        Assert.assertEquals(expected, calculated);
    }

    @Test
    public void testGetReturnIdNull() throws Exception {
        Assert.assertEquals(null, L2Converter.getReturnId(null));
    }

    private void clearPwId(SCreateElineAndTunnelsInput realValue) {
        if (realValue != null
            && realValue.getInput() != null
            && realValue.getInput().getSncEline() != null
            && realValue.getInput().getSncEline().getSncPws() != null) {
            SSncPw sSncPw = realValue.getInput().getSncEline().getSncPws().getSncPw();
            sSncPw.setId(null);
            sSncPw.getQos().setBelongedId(null);
            if (sSncPw.getOam() != null) {
                sSncPw.getOam().setBelongedId(null);
            }
        }
    }
}