Cómo configurar la API Key para usar el servicio de OpenAI en esta aplicación:

1. En la ruta: /openai_resources verifica que exista el archivo openai.secret.properties. 
Si no existe, créalo manualmente con exactamente ese mismo nombre. -> openai.secret.properties

2. Abre el archivo con un editor de texto y agrega la siguiente línea:
api_key=sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

3. Reemplaza sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx con tu API Key real de OpenAI (sin comillas ni símbolos adicionales).
(si no tienes la API KEY, pidesela al integrante más bajito del proyecto, con gusto te la dará)
4. Guarda el archivo.

¡Y listo! Tu API Key está configurada correctamente y la aplicación podrá acceder al servicio de OpenAI.
