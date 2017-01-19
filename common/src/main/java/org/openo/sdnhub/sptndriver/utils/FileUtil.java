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

package org.openo.sdnhub.sptndriver.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * The util class to deal with file I/O.
 */
public class FileUtil {

    private FileUtil(){}

    /**
     * Read file content to a string.
     * @param fileName File name
     * @return File content.
     * @throws IOException When file not exists.
     */
    public static String readFile(String fileName)
        throws IOException {
        return FileUtils.readFileToString(new File(fileName));
    }
}
