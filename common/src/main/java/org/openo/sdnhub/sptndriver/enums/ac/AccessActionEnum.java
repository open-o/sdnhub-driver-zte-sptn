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

package org.openo.sdnhub.sptndriver.enums.ac;

import org.openo.sdnhub.sptndriver.exception.ParamErrorException;
import org.openo.sdnhub.sptndriver.models.south.SServiceEndPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumerator of access action.
 */
public enum AccessActionEnum {
    KEEP("keep", SServiceEndPoint.AccessActionEnum.KEEP),
    PUSH("push", SServiceEndPoint.AccessActionEnum.PUSH),
    POP("pop", SServiceEndPoint.AccessActionEnum.POP),
    SWAP("swap", SServiceEndPoint.AccessActionEnum.SWAP);
    private String north;
    private SServiceEndPoint.AccessActionEnum south;

    AccessActionEnum(String name, SServiceEndPoint.AccessActionEnum index) {
        this.north = name;
        this.south = index;
    }

    /**
     * Convert SBI access action to NBI access action.
     *
     * @param southValue SBI access action.
     * @return NBI access action.
     */
    public static String convertSbiToNbi(SServiceEndPoint.AccessActionEnum southValue) {
        for (AccessActionEnum e : AccessActionEnum.values()) {
            if (e.getSouth().equals(southValue)) {
                return e.north;
            }
        }
        return null;
    }

    /**
     * Convert NBI access action to NBI access action.
     *
     * @param northValue NBI access action.
     * @return SBI access action.
     */
    public static SServiceEndPoint.AccessActionEnum convertNbiToSbi(String northValue)
        throws ParamErrorException {
        if (northValue == null) {
            return null;
        }
        for (AccessActionEnum e : AccessActionEnum.values()) {
            if (e.getNorth().equals(northValue)) {
                return e.south;
            }
        }
        List<String> validValues = new ArrayList<>();
        for (AccessActionEnum accessActionEnum : AccessActionEnum.values()) {
            validValues.add(accessActionEnum.getNorth());
        }
        throw new ParamErrorException(validValues.toArray(), northValue);
    }

    /**
     * Get NBI access action.
     */
    public String getNorth() {
        return north;
    }

    /**
     * Get SBI access action.
     */
    public SServiceEndPoint.AccessActionEnum getSouth() {
        return south;
    }
}
