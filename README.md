# SpyNet🕵️
![image](https://github.com/user-attachments/assets/514b7def-e401-4087-9e45-3096f5b044e2)


Una aplicación desarrollada por el equipo de desarrollo **CatatUNbo** para una empresa de auditoria informática, que permitira varias funcionalidades.

## 🔎 Descripción del proyecto
Spynet es una aplicación de escritorio diseñada para asistir a empresas de auditoría informática en la realización, gestión y documentación de auditorías de seguridad técnica. Su objetivo principal es automatizar y centralizar procesos clave de la auditoría, tales como el escaneo de puertos y servicios, el análisis cualitativo de vulnerabilidades detectadas, la generación de informes técnicos, y el seguimiento estructurado de observaciones.


## 🎯 Objetivos

Con Spynet buscamos:

-Simplificar la gestión de auditorías mediante un sistema que permita registrar, visualizar y organizar auditorías en curso o finalizadas.

-Automatizar tareas técnicas complejas como escaneos de red con Nmap, integrando herramientas de análisis con inteligencia artificial para interpretar los resultados de forma comprensible y profesional.

-Establecer trazabilidad y control de cambios, mediante un historial inmutable de observaciones asociadas a cada auditoría.

-Facilitar la comunicación con los clientes, permitiendo que envíen solicitudes de auditoría directamente desde la plataforma.

-Unificar toda la evidencia técnica y cualitativa en un único informe PDF exportable, optimizando la presentación final de resultados a los clientes.

-Integrar funcionalidades complementarias de análisis contextual, como una sección donde se realiza web scraping del comportamiento actual de criptomonedas (BTC, ETH, etc.) desde Binance. Esta función permite observar correlaciones entre incidentes en activos digitales y posibles vulnerabilidades en sistemas conectados a servicios financieros o Web3.



📦 README - INSTALACIÓN DE DEPENDENCIAS Y EJECUCIÓN DEL SISTEMA


Este proyecto requiere las siguientes dependencias externas:

- ☕ Java Development Kit (JDK) 24
- 🕵️‍♂️ Nmap 7.97
- 🛠️ Apache Maven 3.9.10

Estas herramientas son necesarias para compilar y ejecutar la aplicación correctamente.

Para evitar subir archivos pesados al repositorio (como instaladores .exe o .zip),
el proyecto los descarga automáticamente al ejecutar un script.


⚙️ 1. INSTALACIÓN AUTOMÁTICA DE DEPENDENCIAS


Ejecuta el siguiente archivo en Windows:

    install-external-dependencies.bat

Este script:

- ✅ Verifica si ya existen los instaladores o archivos comprimidos en:
  
      src\main\resources\executables\

- 🌐 Si no están, los descarga usando `curl`:
  
  - JDK 24 desde Oracle.
  - Nmap 7.97 desde nmap.org.
  - Apache Maven 3.9.10 (formato .zip) desde apache.org.

- ▶️ Luego, ejecuta los instaladores `.exe` para JDK y Nmap.
- 🔒 Tendrás que aceptar los permisos de administrador para cada uno.

📌 **¿Qué pasa si ejecutas el script por accidente aunque ya tienes las dependencias instaladas?**

- Si **ya tienes instalados** Java, Nmap o Maven, el script **volverá a ejecutar los instaladores**, lo que puede:
  - ❌ Provocar que se sobrescriban versiones anteriores.
  - ⚠️ Desconfigurar algunas rutas si se cancela la instalación a mitad de camino.
- En ese caso:
  - Puedes **volver a ejecutar el script y completar las instalaciones correctamente**.
  - 🔁 **Es recomendable reiniciar el sistema** para que los cambios tengan efecto.

✅ Consejo: si no estás seguro de si ya lo tienes instalado, puedes verificarlo en consola con:

    java -version
    nmap --version
    mvn -version


🧰 2. INSTALACIÓN MANUAL DE MAVEN DESPUÉS DE LA DESCARGA


Como Maven se descarga en un archivo `.zip`, es necesario:

1. 🗜️ Descomprimir el archivo:

       src\main\resources\executables\apache-maven-3.9.10-bin.zip

2. 📁 Copiar la carpeta descomprimida a:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10

3. ➕ Agregar al `PATH` (de usuario y de sistema) la siguiente ruta:

       C:\Program Files\Apache\Maven\apache-maven-3.9.10\bin

4. ✅ Verificar la instalación ejecutando en consola:

       mvn -version

   Si no aparece, 🔁 **reinicia el sistema** para aplicar las variables de entorno.


🧹 3. LIMPIEZA DE ARCHIVOS DESCARGADOS (ANTES DE COMMIT)


Para evitar subir archivos innecesarios al repositorio Git,
se proporciona un script para eliminar todos los instaladores descargados:

    clean-executables.bat

Este script borra **todos los archivos dentro de**:

    src\main\resources\executables\

⚠️ Esto es importante porque:
- Los archivos instaladores ocupan mucho espacio.
- GitHub **no permite subir archivos de más de 100 MB**.
- Estos archivos **no deben rastrearse en Git**, y por eso están excluidos en el archivo `.gitignore`.


🚀 4. EJECUCIÓN DEL SISTEMA


Una vez instaladas las dependencias (JDK, Nmap y Maven):

1. 🖥️ Abre una consola (CMD o PowerShell).
2. 📂 Navega a la carpeta del proyecto.
3. ▶️ Ejecuta el script:

       run.bat

Este script compilará y ejecutará la aplicación automáticamente.


❓ 5. ¿POR QUÉ SE HACE ASÍ?


Los archivos `.exe` y `.zip` no se incluyen directamente en el repositorio para:

- ⛔ Cumplir con el límite de tamaño de archivos de GitHub (100 MB).
- 💡 Mantener el repositorio liviano y fácil de clonar.
- ✅ Evitar conflictos al versionar binarios o archivos generados automáticamente.

Al usar `curl` para descargar las dependencias cuando se necesitan, se automatiza la preparación del entorno de desarrollo, manteniendo un repositorio limpio y portable.


🎉 ¡Tu entorno ahora estará listo para funcionar!



## 👥 Integrantes del equipo - *CatatUNbo*

-> **Santiago Alejandro Rojas Feo**  
  srojasfe@unal.edu.co

-> **Juan Diego Rozo Álvarez**  
  jurozoa@unal.edu.co

-> **Felipe Rojas Marin**  
  ferojasm@unal.edu.co

-> **Miguel Angel Citarella**  
  mcitarella@unal.edu.co





