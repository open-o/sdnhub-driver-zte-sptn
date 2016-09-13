/*
 * Copyright 2016 ZTE, Inc. and others.
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

package org.openo.sdno.sptndriver.utils;

import org.apache.commons.net.util.SubnetUtils;

/**
 * The Util class to convert IPV4.
 */
public class Ipv4Util {

  /**
   * Get the IP part from the full IP "a.b.c.d/mask".
   *
   * @param ip Full IP address like "a.b.c.d/mask"
   * @return IP address like "a.b.c.d"
   */
  public static String getDotIp(String ip) {
    if (ip != null) {
      String[] parts = ip.split("/");
      if (parts.length > 0) {
        return parts[0];
      }
    }
    return ip;
  }

  /**
   * Get IP mask from full IP "a.b.c.d/mask", and return in forms of "255.255.255.0".
   *
   * @param ip "a.b.c.d/mask"
   * @return IP mask like "255.255.255.0", return "255.255.255.255" if input IP like "a.b.c.d"
   */
  public static String getDotMask(String ip) {
    if (ip == null) {
      return null;
    }
    String mask;

    String[] parts = ip.split("/");
    if (parts.length >= 2) {
      mask = parts[1];
    } else {
      return "255.255.255.255";
    }

    if (mask.contains(".")) {
      return mask;
    }
    return ipInt2DotStr(Integer.parseInt(mask));
  }

  /**
   * Transform int mask to string dot mask.
   *
   * @param intMask Int value of mask, like 24
   * @return Mask in form of "255.255.255.0"
   */
  private static String ipInt2DotStr(int intMask) {
    String cidr = "255.255.255.255/" + intMask;
    SubnetUtils subnet = new SubnetUtils(cidr);
    return subnet.getInfo().getNetmask();
  }
}
