@echo off
set CLOUD_BIN=.\cloud\target
set CLOUD_LIBS=%CLOUD_BIN%\par-provided
set VIRGO=%VIRGO_WEB_SERVER%
set VIRGO_REPO=%VIRGO%\repository\usr
copy %CLOUD_LIBS%\*.* %VIRGO_REPO%
copy %CLOUD_BIN%\cloud.par-1.0.par %VIRGO%\pickup