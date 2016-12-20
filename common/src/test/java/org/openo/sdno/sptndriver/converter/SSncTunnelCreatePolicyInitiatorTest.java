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

import org.junit.Test;
import org.openo.sdno.sptndriver.exception.ParamErrorException;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * The UT class of SSncTunnelCreatePolicyInitiator.
 */
public class SSncTunnelCreatePolicyInitiatorTest {

    @Test
    public void testInputNull() throws Exception {
        try {
            SSncTunnelCreatePolicyInitiator.initTunnelPolicy(null);
        } catch (ParamErrorException ex) {
            assertThat(ex.toString(), containsString("Input tunnel service is null."));
            return;
        }
        fail("Expect ParamErrorException for null input.");
    }
}