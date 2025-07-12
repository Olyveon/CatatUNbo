@echo off
echo ===============================
echo LIMPIANDO INSTALADORES LOCALES
echo ===============================

set "EXEC_DIR=src\main\resources\executables"

:: Eliminar instaladores si existen
if exist "%EXEC_DIR%\jdk-24_windows-x64_bin.exe" (
    del /f "%EXEC_DIR%\jdk-24_windows-x64_bin.exe"
    echo Eliminado: jdk-24_windows-x64_bin.exe
)

if exist "%EXEC_DIR%\nmap-7.97-setup.exe" (
    del /f "%EXEC_DIR%\nmap-7.97-setup.exe"
    echo Eliminado: nmap-7.97-setup.exe
)

echo.
echo Limpieza completada.


@REM "call clean-executables.bat": es necesaria ya que una vez se instalan los programas, los instaladores no sirven de nada. Tambien lo pongo porque si se hace un
@REM commit con los programas de instalación, estos pesan mas de 100MB y git no permite hacer commits con este tamaño de archivos.

@REM si algun archivo de instalacion no se borra adecuadamente e impide hacer un git push, ejecuten por aparte clean-executables.bat
