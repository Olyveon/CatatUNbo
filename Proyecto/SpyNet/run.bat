@echo off
cd /d "%~dp0"
echo Compilando proyecto...
mvn clean compile
if %errorlevel% neq 0 (
    echo Error en la compilacion. Verifica los errores anteriores.
    pause
    exit /b 1
)
echo Ejecutando SpyNet...
mvn exec:java -Dexec.mainClass="org.catatunbo.spynet.App"
pause
