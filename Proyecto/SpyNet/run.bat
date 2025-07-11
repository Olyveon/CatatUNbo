@echo off
cd /d "%~dp0"
mvn exec:java -Dexec.mainClass="org.catatunbo.spynet.App"
pause
