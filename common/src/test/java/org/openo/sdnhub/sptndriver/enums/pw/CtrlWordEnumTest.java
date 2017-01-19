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

package org.openo.sdnhub.sptndriver.enums.pw;

import junit.framework.Assert;

import org.junit.Test;
import org.openo.sdnhub.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.*;

/**
 * The UT class of CtrlWordEnum.
 */
public class CtrlWordEnumTest {

    @Test
    public void testConvertNbiToSbi_legal() throws Exception {
        Assert.assertEquals(CtrlWordEnum.DISABLE.getIndex(),
            CtrlWordEnum.getIndex(CtrlWordEnum.DISABLE.getName()));
    }

    @Test
    public void testConvertNbiToSbi_Illegal() throws Exception {
        try {
            CtrlWordEnum.getIndex("DISABLE");
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Except ParamErrorException when convert DISABLE to SBI");
    }

    @Test
    public void testConvertNbiToSbi_null() throws Exception {
        Assert.assertEquals(null, CtrlWordEnum.getIndex(null));
    }

    @Test
    public void testConvertSbiToNbi_legal() throws Exception {
        Assert.assertEquals(CtrlWordEnum.ENABLE.getName(),
            CtrlWordEnum.getName(CtrlWordEnum.ENABLE.getIndex()));
    }

    @Test
    public void testConvertSbiToNbi_null() throws Exception {
        Assert.assertEquals(null, CtrlWordEnum.getName(null));
    }

    @Test
    public void testConvertSbiToNbi_Illegal() throws Exception {
        try {
            CtrlWordEnum.getName(2);
        } catch (ParamErrorException ex) {
            return;
        }
        fail("Except ParamErrorException when convert 2 to NBI");
    }
}