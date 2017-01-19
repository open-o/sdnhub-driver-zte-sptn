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

import org.openo.sdnhub.sptndriver.exception.ParamErrorException;
import org.openo.sdnhub.sptndriver.models.south.SEncapsulateType;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumerator of encapsulate type.
 */
public enum EncapsulateTypeEnum {
    VLAN("vlan", SEncapsulateType.ETHERNET_VLAN),
    ETHERNET("eth", SEncapsulateType.ETHERNET);
    private String north;
    private SEncapsulateType south;

    EncapsulateTypeEnum(String north, SEncapsulateType south) {
        this.north = north;
        this.south = south;
    }

    /**
     * Convert SBI encapsulate type to NBI encapsulate type.
     *
     * @param south SBI encapsulate type.
     * @return NBI encapsulate type.
     */
    public static String convertSbiToNbi(SEncapsulateType south) {
        for (EncapsulateTypeEnum e : EncapsulateTypeEnum.values()) {
            if (e.getSouth().equals(south)) {
                return e.north;
            }
        }
        return null;
    }

    /**
     * Convert NBI encapsulate type to SBI encapsulate type.
     *
     * @param north NBI encapsulate type.
     * @return SBI encapsulate type.
     */
    public static SEncapsulateType convertNbiToSbi(String north)
        throws ParamErrorException {
        if (north == null) {
            return null;
        }
        for (EncapsulateTypeEnum e : EncapsulateTypeEnum.values()) {
            if (e.getNorth().equals(north)) {
                return e.south;
            }
        }
        List<String> validValues = new ArrayList<>();
        for (EncapsulateTypeEnum encapsulateTypeEnum : EncapsulateTypeEnum.values()) {
            validValues.add(encapsulateTypeEnum.getNorth());
        }
        throw new ParamErrorException(validValues.toArray(), north);
    }

    /**
     * Get NBI encapsulate type.
     */
    public String getNorth() {
        return north;
    }

    /**
     * Get SBI encapsulate type.
     */
    public SEncapsulateType getSouth() {
        return south;
    }
}
