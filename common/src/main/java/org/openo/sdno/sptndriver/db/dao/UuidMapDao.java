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

package org.openo.sdno.sptndriver.db.dao;

import org.openo.sdno.sptndriver.db.dao.mappers.UuidMapMapper;
import org.openo.sdno.sptndriver.db.model.UuidMap;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 *  The DAO class of UUID Map.
 */
public interface UuidMapDao {

  @Mapper(UuidMapMapper.class)
  @SqlQuery("select * from SDNO_ZTE_SPTN_DRIVER_IDMAP where "
      + "UUID = :uuid "
      + "and OBJTYPE = :type "
      + "and CONTROLLERID = :controllerId")
  UuidMap get(@Bind("uuid") String uuid,
              @Bind("type") String type,
              @Bind("controllerId") String controllerId);

  @GetGeneratedKeys
  @SqlUpdate("insert into SDNO_ZTE_SPTN_DRIVER_IDMAP (ID,UUID,EXTERNALID,OBJTYPE,CONTROLLERID)"
      + " values (NULL, :uuid, :externalId, :type, :controllerId)")
  int insert(@Bind("uuid") String uuid,
                    @Bind("externalId") String externalId,
                    @Bind("type") String type,
                    @Bind("controllerId") String controllerId);

  @SqlUpdate("delete from SDNO_ZTE_SPTN_DRIVER_IDMAP where "
      + "UUID = :uuid "
      + "and OBJTYPE = :type "
      + "and CONTROLLERID = :controllerId")
  void delete(@Bind("uuid") String uuid,
              @Bind("type") String type,
              @Bind("controllerId") String controllerId);
}
