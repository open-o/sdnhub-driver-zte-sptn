@REM
@REM Copyright 2016-2017 ZTE Corporation.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM     http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@echo off
call stop.bat
set HOME=%~dp0
set user=%1
set password=%2
set port=3306
set host=127.0.0.1
echo start uninstall sdnhub_driver_zte_sptn
echo HOME=$HOME
cd /d %HOME%
mysql -u%user% -p%password% -P%port% -h%host% < dbscripts\mysql\openo-sdnhub-zte-sptn-driver-clear.sql
set "err=%errorlevel%"
if "%err%"=="0" (
   echo uninstall sdnhub_driver_zte_sptn success
  ) else (
    echo failed uninstall sdnhub_driver_zte_sptn
  )
pause