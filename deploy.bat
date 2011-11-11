@echo off
set CLOUD_BIN=%DROPBOX%\Cloud\virgo\cloud\cloud\target
set CLOUD_LIBS=%CLOUD_BIN%\par-provided
set VIRGO=D:\mr\environment\virgo-tomcat-server-3.0
set VIRGO_REPO=%VIRGO%\repository\usr
copy %CLOUD_LIBS%\*.* %VIRGO_REPO%
copy %CLOUD_BIN%\cloud.par-1.0.par %VIRGO%\pickup