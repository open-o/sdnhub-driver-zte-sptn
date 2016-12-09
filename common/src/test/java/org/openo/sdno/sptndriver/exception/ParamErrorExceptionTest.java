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

package org.openo.sdno.sptndriver.exception;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * The unit test class of ParamErrorException.
 */
public class ParamErrorExceptionTest {
    @Test
    public void testGetResponse_input_notnull() throws Exception {
        List<String> validValueList = new ArrayList<>();
        validValueList.add("validValue1");
        validValueList.add("validValue2");
        String inputValue = "InvalidValue";
        ParamErrorException ex
            = new ParamErrorException(validValueList.toArray(), inputValue);
        Response actualValue = ex.getResponse();

        String expectedValue = "Valid values are: {validValue1,validValue2,}, input value is: InvalidValue. ";

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), actualValue.getStatus());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, actualValue.getMediaType());
        assertEquals(expectedValue, actualValue.getEntity());
    }

    @Test
    public void testToString_input_null() throws Exception {
        List<String> validValueList = new ArrayList<>();
        validValueList.add("validValue1");
        validValueList.add("validValue2");
        ParamErrorException ex
            = new ParamErrorException(validValueList.toArray(), null);
        String expectedValue = "Valid values are: {validValue1,validValue2,}, input value is: null. ";
        assertEquals(expectedValue, ex.toString());
    }

    @Test
    public void testToString_input_string() throws Exception {
        ParamErrorException ex
            = new ParamErrorException("errorInfo");
        String expectedValue = "errorInfo";
        assertEquals(expectedValue, ex.toString());
    }
}