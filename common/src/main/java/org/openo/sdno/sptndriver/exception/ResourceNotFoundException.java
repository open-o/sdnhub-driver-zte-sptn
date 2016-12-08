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

/**
 * Throw this exception if the entity(like L2 or L3) can not be found.
 */
public class ResourceNotFoundException extends Exception {
    private Object resource;

    public ResourceNotFoundException(Object resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Can not find " + resource.toString();
    }

}
