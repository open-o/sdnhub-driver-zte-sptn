--
--
-- Copyright 2016 [ZTE] and others.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

/******************drop old database and user***************************/
use mysql;
drop database IF  EXISTS sdno_driver_zte_sptn;
delete from user where User='sdno_driver_zte_sptn';
FLUSH PRIVILEGES;

/******************create new database and user***************************/
create database sdno_driver_zte_sptn CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON sdno_driver_zte_sptn.* TO 'sdno_driver_zte_sptn'@'%' IDENTIFIED BY 'Test_12345' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON sdno_driver_zte_sptn.* TO 'sdno_driver_zte_sptn'@'localhost' IDENTIFIED BY 'Test_12345' WITH GRANT OPTION;

FLUSH PRIVILEGES;

use sdno_driver_zte_sptn;
set Names 'utf8';
/******************drop old table and create new***************************/

DROP TABLE IF EXISTS IDMAP;
CREATE TABLE IDMAP ( 
  `ID` integer AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `UUID` varchar(255) NOT NULL, 
  `EXTERNALID` varchar(255) NOT NULL, 
  `OBJTYPE` varchar(255) NOT NULL, 
  `CONTROLLERID` varchar(255) NOT NULL 
);


