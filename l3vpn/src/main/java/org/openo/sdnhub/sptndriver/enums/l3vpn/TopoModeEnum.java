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

package org.openo.sdnhub.sptndriver.enums.l3vpn;


import org.openo.sdnhub.sptndriver.models.south.SL3vpn;

/**
 * Enumerator of topology mode.
 */
public enum TopoModeEnum {
    SPECIFIED("none", SL3vpn.TopoModeEnum.SPECIFIED),
    ANY_TO_ANY("full-mesh", SL3vpn.TopoModeEnum.ANY_TO_ANY),
    HUB_SPKOE("hub-spoke", SL3vpn.TopoModeEnum.HUB_SPOKE);
    private String north;
    private SL3vpn.TopoModeEnum south;

    TopoModeEnum(String north, SL3vpn.TopoModeEnum south) {
        this.north = north;
        this.south = south;
    }

    /**
     * Convert SBI topology mode to NBI topology mode.
     *
     * @param southValue SBI topology mode
     * @return NBI topology mode
     */
    public static String convertSbiToNbi(SL3vpn.TopoModeEnum southValue) {
        for (TopoModeEnum adminStatusEnum : TopoModeEnum.values()) {
            if (adminStatusEnum.getSouthValue().equals(southValue)) {
                return adminStatusEnum.north;
            }
        }
        return null;
    }

    /**
     * Convert NBI topology mode to SBI topology mode.
     *
     * @param north NBI topology mode
     * @return SBI topology mode
     */
    public static SL3vpn.TopoModeEnum convertNbiToSbi(String north) {
        for (TopoModeEnum adminStatusEnum : TopoModeEnum.values()) {
            if (adminStatusEnum.getNorthValue().equals(north)) {
                return adminStatusEnum.south;
            }
        }
        return null;
    }

    /**
     * Get NBI topology mode.
     */
    public String getNorthValue() {
        return north;
    }

    /**
     * Get SBI topology mode.
     */
    public SL3vpn.TopoModeEnum getSouthValue() {
        return south;
    }
}
