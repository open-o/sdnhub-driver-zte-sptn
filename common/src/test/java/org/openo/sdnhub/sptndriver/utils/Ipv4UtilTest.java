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

package org.openo.sdnhub.sptndriver.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The class to test Ipv4Util.
 */
public class Ipv4UtilTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getDotIp() throws Exception {
        String ip = "1.2.3.4/111";
        String destIp = Ipv4Util.getDotIp(ip);
        Assert.assertEquals("1.2.3.4", destIp);
    }

    @Test
    public void getDotMask() throws Exception {
        String ip = "1.2.3.4/24";
        String destMask = Ipv4Util.getDotMask(ip);
        Assert.assertEquals("255.255.255.0", destMask);
    }

    @Test
    public void testGetDotIpNull() {
        String ip = null;
        String destIp = Ipv4Util.getDotIp(ip);
        Assert.assertEquals(null, destIp);
    }

    @Test
    public void testGetDotMask_MaskIsDot() {
        String ip = "1.2.3.4/255.255.255.0";
        String destMask = Ipv4Util.getDotMask(ip);
        Assert.assertEquals("255.255.255.0", destMask);
    }

    @Test
    public void testGetDotMask_InputNull() {
        String ip = null;
        String destMask = Ipv4Util.getDotMask(null);
        Assert.assertEquals(null, destMask);
    }

    @Test
    public void testGetDotMask_InputMax() {
        String ip = "1.2.3.4/32";
        String destMask = Ipv4Util.getDotMask(ip);
        Assert.assertEquals("255.255.255.255", destMask);
    }

    @Test
    public void testGetDotMask_InputMin() {
        String ip = "1.2.3.4/1";
        String destMask = Ipv4Util.getDotMask(ip);
        Assert.assertEquals("128.0.0.0", destMask);
    }

    @Test
    public void testGetDotMask_NoMask() {
        String ip = "1.2.3.4";
        String destMask = Ipv4Util.getDotMask(ip);
        Assert.assertEquals("255.255.255.255", destMask);
    }
}