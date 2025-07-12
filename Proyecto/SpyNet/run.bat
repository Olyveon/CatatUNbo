@echo off
cd /d "%~dp0"
rem echo Compilando proyecto...
rem mvn clean compile
rem if %errorlevel% neq 0 (
rem    echo Error en la compilacion. Verifica los errores anteriores.
rem    pause
rem    exit /b 1
rem )
rem echo Ejecutando SpyNet...
mvn exec:java -Dexec.mainClass="org.catatunbo.spynet.App"
pause
