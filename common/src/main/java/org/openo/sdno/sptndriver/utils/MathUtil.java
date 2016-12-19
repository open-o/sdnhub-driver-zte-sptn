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

package org.openo.sdno.sptndriver.utils;

/**
 * The Math utility class.
 */
public class MathUtil {

    private MathUtil(){}

    /**
     * Calculate the smallest value that is equal to or bigger than the result of divide.
     *
     * @param dividend The dividend.
     * @param divider  The divider.
     * @return the smallest value that is equal to or bigger than the result of divide.
     */
    public static long ceil(float dividend, float divider) {
        return (long) Math.ceil(dividend / divider);
    }
}
