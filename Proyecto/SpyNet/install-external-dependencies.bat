@echo off
echo ===============================
echo INSTALADOR DE DEPENDENCIAS
echo ===============================
echo.

:: Descargar Java si no existe
if not exist "src\main\resources\executables\jdk-24_windows-x64_bin.exe" (
    echo Descargando Java 24...
    curl.exe -L -o "src\main\resources\executables\jdk-24_windows-x64_bin.exe" "https://download.oracle.com/java/24/latest/jdk-24_windows-x64_bin.exe"
) else (
    echo [OK] Java 24 ya está descargado.
)

:: Descargar Nmap si no existe
if not exist "src\main\resources\executables\nmap-7.97-setup.exe" (
    echo Descargando Nmap 7.97...
    curl.exe -L -o "src\main\resources\executables\nmap-7.97-setup.exe" "https://nmap.org/dist/nmap-7.97-setup.exe"
) else (
    echo [OK] Nmap ya está descargado.
)

echo.
echo ===============================
echo INSTALANDO PROGRAMAS
echo Por favor, acepte permisos de Administrador si se le solicita.
echo ===============================
echo.

:: Ejecutar los instaladores
start "" "src\main\resources\executables\jdk-24_windows-x64_bin.exe"
start "" "src\main\resources\executables\nmap-7.97-setup.exe"

echo.
echo ===============================
echo PROCESO COMPLETADO
echo ===============================


pause
