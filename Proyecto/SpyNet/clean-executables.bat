@echo off
echo ===============================
echo LIMPIANDO INSTALADORES LOCALES
echo ===============================

set "EXEC_DIR=src\main\resources\executables"

:: Eliminar todos los archivos excepto .gitkeep
for %%f in ("%EXEC_DIR%\*") do (
    if /I not "%%~nxf"==".gitkeep" (
        del /f /q "%%f"
        echo Eliminado: %%~nxf
    )
)


echo.
echo Limpieza completada.


@REM "call clean-executables.bat": es necesaria ya que una vez se instalan los programas, los instaladores no sirven de nada. Tambien lo pongo porque si se hace un
@REM commit con los programas de instalación, estos pesan mas de 100MB y git no permite hacer commits con este tamaño de archivos.

@REM si algun archivo de instalacion no se borra adecuadamente e impide hacer un git push, ejecuten por aparte clean-executables.bat
