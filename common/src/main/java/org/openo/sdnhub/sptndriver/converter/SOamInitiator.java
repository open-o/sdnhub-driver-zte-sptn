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

package org.openo.sdnhub.sptndriver.converter;

import org.openo.sdnhub.sptndriver.models.south.SMep;
import org.openo.sdnhub.sptndriver.models.south.SMeps;
import org.openo.sdnhub.sptndriver.models.south.SOam;
import org.openo.sdnhub.sptndriver.models.south.SOamMode;
import org.openo.sdnhub.sptndriver.models.south.STrafficClass;

/**
 * The class to initiate OAM parameters.
 */
public class SOamInitiator {

    private SOamInitiator(){}

    /**
     * Initialization of OAM.
     *
     * @param sncId OAM related connection ID.
     * @return OAM
     */
    public static SOam initOam(String sncId) {
        SOam oam = new SOam();
        oam.setBelongedId(sncId);
        oam.setName(null);
        oam.setMegId("-1");
        oam.setMeps(initMeps());
        oam.setMeps(null);
        oam.setCcAllow(true);
        oam.setCcExp(STrafficClass.CS7);
        oam.setCcInterval(SOam.CcIntervalEnum.NUMBER_3_DOT_3);
        oam.setLmMode(SOamMode.DISABLE);
        oam.setDmMode(SOamMode.DISABLE);
        return oam;
    }

    /**
     * Initialize Mep list of OAM.
     *
     * @return Mep list
     */
    private static SMeps initMeps() {
        SMep mep1 = new SMep();
        mep1.setId("1");
        SMep mep2 = new SMep();
        mep2.setId("2");
        SMeps meps = new SMeps();
        meps.add(mep1);
        meps.add(mep2);
        return meps;
    }
}
