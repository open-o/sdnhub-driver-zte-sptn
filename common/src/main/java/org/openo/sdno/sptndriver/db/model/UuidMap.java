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

package org.openo.sdno.sptndriver.db.model;

/**
 * The class to store the relationship between UUID and external id.
 */
public class UuidMap {

    private String uuid;
    private String externalId;
    private UuidTypeEnum type;
    private String controllerId;

    public UuidMap(String uuid, String externalId, UuidTypeEnum type, String controllerId) {
        this.uuid = uuid;
        this.externalId = externalId;
        this.type = type;
        this.controllerId = controllerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UuidTypeEnum getType() {
        return type;
    }

    public void setType(UuidTypeEnum type) {
        this.type = type;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public enum UuidTypeEnum {
        ELINE, L3VPN
    }

}


