#!/bin/bash
cd "$(dirname "$0")"
echo "Compilando proyecto..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "Error en la compilación. Verifica los errores anteriores."
    exit 1
fi
echo "Ejecutando SpyNet..."
mvn exec:java -Dexec.mainClass="org.catatunbo.spynet.App"
