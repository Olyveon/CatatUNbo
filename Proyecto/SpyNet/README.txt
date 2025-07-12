
===========================================================
README - INSTALACIÓN DE DEPENDENCIAS Y EJECUCIÓN DEL SISTEMA
===========================================================

Este proyecto requiere Java 24, Nmap y Apache Maven para ejecutarse correctamente.
Por eficiencia, mantenibilidad y tamaño del repositorio, los archivos instaladores
de Java y Nmap NO se suben directamente a Git, y se descargan dinámicamente cuando se necesiten.

====================================================
1. INSTALACIÓN DE DEPENDENCIAS (JAVA 24 Y NMAP)
====================================================

Se incluye un script automatizado para descargar e instalar:

- Java Development Kit 24
- Nmap 7.97

Para ejecutarlo, usa el siguiente archivo:

    install-external-dependencies.bat

Este script:
- Verifica si los instaladores ya están presentes en:
  
      src\main\resources\executables\

- Si no lo están, los descarga automáticamente usando `curl`.
- Luego ejecuta los instaladores para que completes la instalación manualmente (gráficamente).

Este enfoque evita tener archivos de gran tamaño (como `.exe`) en el repositorio Git,
los cuales complican los commits y aumentan innecesariamente el peso del proyecto.

IMPORTANTE:
- Ejecuta este archivo **como administrador**.
- Acepta los permisos para instalar los programas cuando se abran las ventanas.

====================================================
2. INSTALACIÓN DE MAVEN 3.9.10
====================================================

1. Asegúrate de tener el directorio `apache-maven-3.9.10` listo (puede estar en el proyecto o descargarse manualmente).
2. Copia esa carpeta a:

       C:\Program Files\Apache\Maven

   De modo que la ruta del ejecutable sea:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10\bin

3. Luego, **agrega esa ruta al PATH** tanto del sistema como del usuario:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10\bin

   Puedes hacerlo manualmente desde:
   - Panel de control → Sistema → Configuración avanzada del sistema → Variables de entorno
   - O usar un script `.bat` preparado para automatizarlo.

4. Para verificar que Maven esté correctamente instalado, abre una terminal nueva y ejecuta:

       mvn -version

   Deberías ver la versión de Maven y la de Java. Si no funciona, reinicia la terminal (o el sistema).

====================================================
3. LIMPIEZA DE INSTALADORES GRANDES (Opcional)
====================================================

Si ya no necesitas los instaladores `.exe`, o necesitas limpiar tu proyecto para hacer commit en Git, puedes ejecutar:

    clean-executables.bat

Este script elimina los archivos:

    src\main\resources\executables\jdk-24_windows-x64_bin.exe
    src\main\resources\executables\nmap-7.97-setup.exe

Esto es útil antes de subir cambios al repositorio y mantenerlo ligero.

Además, estos archivos están ignorados en `.gitignore` para evitar que se suban accidentalmente.

====================================================
4. EJECUCIÓN DE LA APLICACIÓN
====================================================

Una vez instaladas todas las dependencias (Java, Nmap y Maven),
puedes ejecutar la aplicación con:

    run.bat

Este script compila y ejecuta la aplicación principal.

====================================================
¡Y eso es todo! Tu entorno ahora estará listo para funcionar.
====================================================
