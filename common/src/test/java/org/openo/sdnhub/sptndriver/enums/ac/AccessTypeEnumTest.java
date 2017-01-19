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

package org.openo.sdnhub.sptndriver.enums.ac;

import junit.framework.Assert;

import org.junit.Test;
import org.openo.sdnhub.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.*;

/**
 * The UT class of AccessTypeEnum.
 */
public class AccessTypeEnumTest {

    @Test
    public void testConvertNbiToSbi_legal() throws Exception {
        Assert.assertEquals(AccessTypeEnum.DOT1Q.getSouth(),
            AccessTypeEnum.convertNbiToSbi(AccessTypeEnum.DOT1Q.getNorth()));
    }

    @Test
    public void testConvertNbiToSbi_Illegal() throws Exception {
        try {
            AccessTypeEnum.convertNbiToSbi("DOT1Q");
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Except ParamErrorException when convert DOT1Q to SBI");
    }

    @Test
    public void testConvertNbiToSbi_null() throws Exception {
        Assert.assertEquals(null, AccessTypeEnum.convertNbiToSbi(null));
    }

    @Test
    public void testConvertSbiToNbi_legal() throws Exception {
        Assert.assertEquals(AccessTypeEnum.DOT1Q.getNorth(),
            AccessTypeEnum.convertSbiToNbi(AccessTypeEnum.DOT1Q.getSouth()));
    }

    @Test
    public void testConvertSbiToNbi_null() throws Exception {
        Assert.assertEquals(null, AccessTypeEnum.convertSbiToNbi(null));
    }

    @Test
    public void testConvertSbiToNbi_Illegal() throws Exception {
        // Currently, there is no way to input illegal parameter.
    }
}