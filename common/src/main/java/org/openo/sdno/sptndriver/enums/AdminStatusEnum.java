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

import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.south.SAdminStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumerator of administrative status.
 */
public enum AdminStatusEnum {
    UP("adminUp", SAdminStatus.UP),
    DOWN("adminDown", SAdminStatus.DOWN);
    private String north;
    private SAdminStatus south;

    AdminStatusEnum(String north, SAdminStatus south) {
        this.north = north;
        this.south = south;
    }

    /**
     * Convert SBI administrative status to NBI administrative status.
     *
     * @param southValue SBI administrative status
     * @return NBI administrative status
     */
    public static String convertSbiToNbi(SAdminStatus southValue)
        throws ParamErrorException {
        for (AdminStatusEnum adminStatusEnum : AdminStatusEnum.values()) {
            if (adminStatusEnum.getSouthValue().equals(southValue)) {
                return adminStatusEnum.north;
            }
        }
        return null;
    }

    /**
     * Convert NBI administrative status to SBI administrative status.
     *
     * @param north NBI administrative status
     * @return SBI administrative status
     */
    public static SAdminStatus convertNbiToSbi(String north) throws ParamErrorException {
        if (north == null) {
            return null;
        }
        for (AdminStatusEnum adminStatusEnum : AdminStatusEnum.values()) {
            if (adminStatusEnum.getNorthValue().equals(north)) {
                return adminStatusEnum.south;
            }
        }
        List<String> validValues = new ArrayList<>();
        for (AdminStatusEnum adminStatusEnum : AdminStatusEnum.values()) {
            validValues.add(adminStatusEnum.getNorthValue());
        }
        throw new ParamErrorException(validValues.toArray(), north);
    }

    /**
     * Get NBI administrative status.
     */
    public String getNorthValue() {
        return north;
    }

    /**
     * Get SBI administrative status.
     */
    public SAdminStatus getSouthValue() {
        return south;
    }
}
