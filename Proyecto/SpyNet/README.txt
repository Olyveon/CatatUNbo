===========================================================
README - INSTALACIÓN DE DEPENDENCIAS Y EJECUCIÓN DEL SISTEMA
===========================================================

Este proyecto requiere las siguientes dependencias externas:

- Java Development Kit (JDK) 24
- Nmap 7.97
- Apache Maven 3.9.10

Estas herramientas son necesarias para compilar y ejecutar la aplicación correctamente.

Para evitar subir archivos pesados al repositorio (como instaladores .exe o .zip),
el proyecto los descarga automáticamente al ejecutar un script.

====================================================
1. INSTALACIÓN AUTOMÁTICA DE DEPENDENCIAS
====================================================

Ejecuta el siguiente archivo en Windows:

    install-external-dependencies.bat

Este script:

- Verifica si ya existen los instaladores o archivos comprimidos en:
  
      src\main\resources\executables\

- Si no están, los descarga usando `curl`:
  
  - JDK 24 desde Oracle.
  - Nmap 7.97 desde nmap.org.
  - Apache Maven 3.9.10 (formato .zip) desde apache.org.

- Luego, ejecuta los instaladores `.exe` para JDK y Nmap.
- Tendras que aceptar el permiso de administrador para cada instalador.
NOTA: si ya tenías instaladas estas dependencias y por error aceptaste volverlas a instalar
y abortaste el intento, es posible que se te halla borrado la dependencia, por lo que
será necesario reinstalarla. El Script de install-external-dependencies.bat puede ayudarte nuevamente con ello.

====================================================
2. INSTALACIÓN MANUAL DE MAVEN DESPUÉS DE LA DESCARGA
====================================================

Como Maven se descarga en un archivo `.zip`, es necesario:

1. Descomprimir el archivo:

       src\main\resources\executables\apache-maven-3.9.10-bin.zip

2. Copiar la carpeta descomprimida a:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10

3. Agregar al PATH (de usuario y del sistema) la ruta del ejecutable:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10\bin

4. Para verificar que Maven esté instalado correctamente, abre una consola nueva y ejecuta:

       mvn -version

   Si no aparece, reinicia el sistema.

====================================================
3. LIMPIEZA DE ARCHIVOS DESCARGADOS (ANTES DE COMMIT)
====================================================

Para evitar subir archivos innecesarios al repositorio Git,
se proporciona un script para eliminar todos los instaladores descargados:

    clean-executables.bat

Este script elimina todo el contenido de:

    src\main\resources\executables\

⚠️ Estos archivos no deben subirse, ya que ocupan mucho espacio y GitHub limita archivos individuales a 100 MB.

El archivo `.gitignore` ya está configurado para **ignorar todos los archivos** dentro de esa carpeta.

====================================================
4. EJECUCIÓN DEL SISTEMA
====================================================

Una vez instaladas las dependencias (JDK, Nmap y Maven):

1. Abre una consola.
2. Navega a la carpeta del proyecto.
3. Ejecuta el script:

       run.bat

Este script compilará y ejecutará la aplicación automáticamente.

====================================================
5. ¿POR QUÉ SE HACE ASÍ?
====================================================

Los archivos `.exe` y `.zip` no se incluyen en el repositorio para:

- Cumplir con el límite de tamaño de archivos de GitHub (100 MB).
- Mantener el repositorio ligero y rápido para clonar y trabajar.
- Evitar conflictos innecesarios al versionar binarios.

Al usar `curl`, se automatiza la descarga de dependencias externas sin afectar el control de versiones.

====================================================
¡Tu entorno ahora estará listo para funcionar!
====================================================
