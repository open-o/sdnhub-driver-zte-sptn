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

package org.openo.sdno.sptndriver.enums;

import junit.framework.Assert;

import org.junit.Test;
import org.openo.sdno.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.*;

/**
 * The UT class of OperateStatusEnum.
 */
public class OperateStatusEnumTest {
    @Test
    public void testConvertNbiToSbi_legal() throws Exception {
        Assert.assertEquals(OperateStatusEnum.DOWN.getSouthValue(),
            OperateStatusEnum.convertNbiToSbi(OperateStatusEnum.DOWN.getNorthValue()));
    }

    @Test
    public void testConvertNbiToSbi_Illegal() throws Exception {
        try {
            OperateStatusEnum.convertNbiToSbi("operateup");
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Except ParamErrorException when convert operateup to SBI");
    }

    @Test
    public void testConvertNbiToSbi_null() throws Exception {
        Assert.assertEquals(null, OperateStatusEnum.convertNbiToSbi(null));
    }

    @Test
    public void testConvertSbiToNbi_legal() throws Exception {
        Assert.assertEquals(OperateStatusEnum.UP.getNorthValue(),
            OperateStatusEnum.convertSbiToNbi(OperateStatusEnum.UP.getSouthValue()));
    }

    @Test
    public void testConvertSbiToNbi_null() throws Exception {
        Assert.assertEquals(null, OperateStatusEnum.convertSbiToNbi(null));
    }

    @Test
    public void testConvertSbiToNbi_Illegal() throws Exception {
        // Currently, there is no way to input illegal parameter.
    }
}