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

package org.openo.sdno.sptndriver.enums.pw;

import junit.framework.Assert;

import org.junit.Test;
import org.openo.sdno.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.*;

/**
 * The UT class of EncapsulateTypeEnum.
 */
public class EncapsulateTypeEnumTest {

    @Test
    public void testConvertNbiToSbi_legal() throws Exception {
        Assert.assertEquals(EncapsulateTypeEnum.ETHERNET.getSouth(),
            EncapsulateTypeEnum.convertNbiToSbi(EncapsulateTypeEnum.ETHERNET.getNorth()));
    }

    @Test
    public void testConvertNbiToSbi_Illegal() throws Exception {
        try {
            EncapsulateTypeEnum.convertNbiToSbi("ETHERNET");
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Except ParamErrorException when convert ETHERNET to SBI");
    }

    @Test
    public void testConvertNbiToSbi_null() throws Exception {
        Assert.assertEquals(null, EncapsulateTypeEnum.convertNbiToSbi(null));
    }

    @Test
    public void testConvertSbiToNbi_legal() throws Exception {
        Assert.assertEquals(EncapsulateTypeEnum.VLAN.getNorth(),
            EncapsulateTypeEnum.convertSbiToNbi(EncapsulateTypeEnum.VLAN.getSouth()));
    }

    @Test
    public void testConvertSbiToNbi_null() throws Exception {
        Assert.assertEquals(null, EncapsulateTypeEnum.convertSbiToNbi(null));
    }

    @Test
    public void testConvertSbiToNbi_Illegal() throws Exception {
        // Currently, there is no way to input illegal parameter.
    }
}