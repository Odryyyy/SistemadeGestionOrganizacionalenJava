@echo off
cd /d "%~dp0"
set CP=build\classes;TimingFramework-0.55.jar;miglayout-4.0.jar;lib\*
java -cp "%CP%" com.raven.main.Main
pause
