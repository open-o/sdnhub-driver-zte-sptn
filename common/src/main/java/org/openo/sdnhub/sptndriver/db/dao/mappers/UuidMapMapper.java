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

package org.openo.sdnhub.sptndriver.db.dao.mappers;

import org.openo.sdnhub.sptndriver.db.model.UuidMap;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The mapper class of UUID map.
 */
public class UuidMapMapper implements ResultSetMapper<UuidMap> {
    @Override
    public UuidMap map(int index, ResultSet resultSet, StatementContext statementContext)
        throws SQLException {
        return new UuidMap(resultSet.getString("UUID"),
            resultSet.getString("EXTERNALID"),
            UuidMap.UuidTypeEnum.valueOf(resultSet.getString("OBJTYPE")),
            resultSet.getString("CONTROLLERID"));
    }
}
