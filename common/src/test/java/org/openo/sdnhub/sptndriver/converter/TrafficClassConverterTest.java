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
import org.openo.sdnhub.sptndriver.models.south.STrafficClass;
import org.openo.sdnhub.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.fail;

/**
 * The UT of TrafficClassConverter
 */
public class TrafficClassConverterTest {

    @Test
    public void testNull() throws Exception {
        Assert.assertEquals(null, TrafficClassConverter.getEnum(null));
    }

    @Test
    public void testNormalEnum() throws Exception {
        Assert.assertEquals(STrafficClass.BE, TrafficClassConverter.getEnum("BE"));
    }

    @Test
    public void testIllegalEnum() throws Exception {
        String trafficClassName = "be";
        try {
            TrafficClassConverter.getEnum(trafficClassName);
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Expect ParamErrorException for illegal traffic class: " + trafficClassName);
    }

}