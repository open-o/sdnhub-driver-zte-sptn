--
--
-- Copyright 2016-2017 ZTE Corporation.
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
drop database IF  EXISTS sdnhub_driver_zte_sptn;
delete from user where User='sptn_driver';
FLUSH PRIVILEGES;

/******************create new database and user***************************/
create database sdnhub_driver_zte_sptn CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON sdnhub_driver_zte_sptn.* TO 'sptn_driver'@'%' IDENTIFIED BY 'Test_12345' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON sdnhub_driver_zte_sptn.* TO 'sptn_driver'@'localhost' IDENTIFIED BY 'Test_12345' WITH GRANT OPTION;

FLUSH PRIVILEGES;

use sdnhub_driver_zte_sptn;
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


