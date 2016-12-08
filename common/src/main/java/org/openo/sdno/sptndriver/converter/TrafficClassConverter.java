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

import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.south.STrafficClass;

import java.util.ArrayList;
import java.util.List;

/**
 * The class to convert traffic class enumerator.
 */
public class TrafficClassConverter {

    /**
     * Convert string to SBI traffic class enumerator.
     *
     * @param trafficClass String traffic class.
     * @return Enumerator traffic class, return null if input is illegal.
     */
    public static STrafficClass getEnum(String trafficClass)
        throws ParamErrorException {
        if (trafficClass == null) {
            return null;
        }
        for (STrafficClass enumTrafficClass : STrafficClass.values()) {
            if (enumTrafficClass.toString().equals(trafficClass)) {
                return enumTrafficClass;
            }
        }
        List<String> validValues = new ArrayList<>();
        for (STrafficClass accessActionEnum : STrafficClass.values()) {
            validValues.add(accessActionEnum.toString());
        }
        throw new ParamErrorException(validValues.toArray(), trafficClass);
    }

}
