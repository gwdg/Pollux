@echo off

:MENU
cls
echo          =====================
echo          º    UDO LICENSE    º
echo          =====================
echo          º  0. Quit          º
echo          º  1. Check         º
echo          º  2. Format        º
echo          º  3. Remove        º
echo          =====================
set /p userinp=License Command?"(0-3):
set userinp=%userinp:~0,1%

if "%userinp%"=="0" goto END
if "%userinp%"=="1" goto check
if "%userinp%"=="2" goto format
if "%userinp%"=="3" goto remove
goto MENU

:check
mvn license:check -Dyear=2011 -Dorganisation="X-Juvi"
goto MENU

:format
mvn license:format -Dyear=2011 -Dorganisation="X-Juvi"
goto MENU

:remove
mvn license:remove -Dyear=2011 -Dorganisation="X-Juvi"
goto MENU

:END
cls