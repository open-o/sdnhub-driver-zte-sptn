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

package org.openo.sdnhub.sptndriver.enums.south.pw;

import junit.framework.Assert;

import org.junit.Test;

/**
 * The UT class of SPwRole.
 */
public class SSnSupportTest {
    @Test
    public void testToString_0() throws Exception {
        Assert.assertEquals("0", SSnSupport.NOT_SUPPORT.toString());
    }

    @Test
    public void testToString_1() throws Exception {
        Assert.assertEquals("1",SSnSupport.SUPPORT.toString());
    }
}