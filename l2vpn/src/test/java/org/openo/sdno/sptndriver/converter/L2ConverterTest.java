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
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnelsInput;
import org.openo.sdno.sptndriver.models.south.SSncPw;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;

/**
 * The class to test L2Convert
 */
public class L2ConverterTest {
  @Test
  public void convertL2ToElineTunnerCreator() throws Exception {
    File inputJson = new File("src/test/resource/json/create_l2vpn_input.json");
    JsonParser crtParser = new JsonParser();
    JsonElement crtBody = crtParser.parse(new FileReader(inputJson));

    Gson inputGson = new Gson();
    Type l2vpnType = new TypeToken<NL2Vpn>(){}.getType();
    NL2Vpn l2vpn = inputGson.fromJson(crtBody, l2vpnType);

    SCreateElineAndTunnelsInput calculatedOutput = L2Converter.convertL2ToElineTunnerCreator(l2vpn);

    File outputJson = new File("src/test/resource/json/create_l2vpn_output.json");
    JsonParser routeParser = new JsonParser();
    JsonElement outputBody = routeParser.parse(new FileReader(outputJson));

    Gson outputGson = new Gson();
    Type SCreateElineAndTunnelsInput = new TypeToken<SCreateElineAndTunnelsInput>(){}.getType();
    SCreateElineAndTunnelsInput expectedOutput = outputGson.fromJson(outputBody, SCreateElineAndTunnelsInput);

    // uuid of pw is different every time, so must clear the uuid.
    clearPwId(calculatedOutput);
    clearPwId(expectedOutput);
    Assert.assertEquals(calculatedOutput, expectedOutput);
  }

  @Test
  public void getReturnId() throws Exception {

  }

  private void clearPwId(SCreateElineAndTunnelsInput realValue) {
    if (realValue != null
        && realValue.getInput() != null
        && realValue.getInput().getSncEline() != null
        && realValue.getInput().getSncEline().getSncPws() != null) {
      SSncPw sSncPw = realValue.getInput().getSncEline().getSncPws().getSncPw();
      sSncPw.setId(null);
      sSncPw.getQos().setBelongedId(null);
      sSncPw.getOam().setBelongedId(null);
    }
  }
}