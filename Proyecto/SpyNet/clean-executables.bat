@echo off
echo ===============================
echo LIMPIANDO INSTALADORES LOCALES
echo ===============================

set "EXEC_DIR=src\main\resources\executables"


if exist "%EXEC_DIR%" (
    echo Eliminando todo el contenido de: %EXEC_DIR%
    for /d %%i in ("%EXEC_DIR%\*") do rd /s /q "%%i"
    del /q "%EXEC_DIR%\*" >nul
    echo Contenido eliminado.
) else (
    echo La carpeta "%EXEC_DIR%" no existe.
)


echo.
echo Limpieza completada.


@REM "call clean-executables.bat": es necesaria ya que una vez se instalan los programas, los instaladores no sirven de nada. Tambien lo pongo porque si se hace un
@REM commit con los programas de instalación, estos pesan mas de 100MB y git no permite hacer commits con este tamaño de archivos.

@REM si algun archivo de instalacion no se borra adecuadamente e impide hacer un git push, ejecuten por aparte clean-executables.bat
