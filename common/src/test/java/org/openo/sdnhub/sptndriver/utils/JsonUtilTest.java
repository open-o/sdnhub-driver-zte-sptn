/*
 * Copyright 2017 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdnhub.sptndriver.utils;

import com.google.gson.annotations.SerializedName;


import org.junit.Assert;
import org.junit.Test;

/**
 * The UT class of JsonUtil
 */
public class JsonUtilTest {
    @Test
    public void testReadJsonFromFile() throws Exception {
        Object obj = JsonUtil.readJsonFromFile("src/test/resources/json/utils/JsonUtil.json");
        Assert.assertNotNull(obj);
    }

    @Test
    public void testParseJsonFromFile() throws Exception {
        JsonDemo jsonDemo = JsonUtil.parseJsonFromFile("src/test/resources/json/utils/JsonUtil.json", JsonDemo.class);
        Assert.assertNotNull(jsonDemo);
    }

    private class JsonDemo {
        @SerializedName("test-name")
        private String testName;
    }
}