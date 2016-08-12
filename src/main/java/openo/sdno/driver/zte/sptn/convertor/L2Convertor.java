package openo.sdno.driver.zte.sptn.convertor;

import openo.sdno.driver.zte.sptn.models.north.L2Vpn;
import openo.sdno.driver.zte.sptn.models.south.CreateElineAndTunnels;

/**
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
public class L2Convertor {

  public static CreateElineAndTunnels L2ToElineTunnerCreator(L2Vpn l2vpn) {
    CreateElineAndTunnels createElineAndTunnels = new CreateElineAndTunnels();
    return createElineAndTunnels;
  }
}
