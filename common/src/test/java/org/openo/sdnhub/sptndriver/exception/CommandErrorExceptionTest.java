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

package org.openo.sdnhub.sptndriver.exception;

import org.junit.Test;
import org.openo.sdnhub.sptndriver.models.south.SCommandResult;
import org.openo.sdnhub.sptndriver.models.south.SFailedResource;
import org.openo.sdnhub.sptndriver.models.south.SFailedResources;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * The unit test class of CommandErrorException.
 */
public class CommandErrorExceptionTest {
    @Test
    public void testGetResponse() throws Exception {
        CommandErrorException ex = new CommandErrorException(null);
        Response actualValue = ex.getResponse();

        assertEquals(Response.Status.NOT_IMPLEMENTED.getStatusCode(), actualValue.getStatus());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, actualValue.getMediaType());
        assertEquals("Error information from controller is null.", actualValue.getEntity());
    }

    @Test
    public void testGetErrorInfo_failedResources_null() throws Exception {
        SCommandResult commandResult = new SCommandResult();
        CommandErrorException ex = new CommandErrorException(commandResult);
        assertEquals("Error information from controller is null.", ex.toString());
    }

    @Test
    public void testGetErrorInfo_failedResources_empty() throws Exception {
        SCommandResult commandResult = new SCommandResult();
        commandResult.setFailedResources(new SFailedResources());
        CommandErrorException ex = new CommandErrorException(commandResult);
        assertEquals("Error information from controller is null.", ex.toString());
    }

    @Test
    public void testGetErrorInfo_failedResources_notEmpty() throws Exception {
        SFailedResource failedResource = new SFailedResource();
        failedResource.setErrorMessage("Controller Error Message");
        failedResource.setErrorCode("Controller Error code");
        failedResource.setResouceId("Resource id");
        SFailedResources failedResources = new SFailedResources();
        failedResources.getFailedResourceList().add(failedResource);
        SCommandResult commandResult = new SCommandResult();
        commandResult.setFailedResources(failedResources);
        CommandErrorException ex = new CommandErrorException(commandResult);

        assertEquals("Controller returns failure: Controller Error Message", ex.toString());
    }
}