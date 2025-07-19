package org.catatunbo.spynet.auditUtils;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
// import com.openai.models.ChatModel;
import com.openai.models.ChatCompletionCreateParams;

import java.util.function.Consumer;

// import org.catatunbo.spynet.auditUtils.OpenAIConfig;

public class OpenAI {
        
        private String contentInput="";
        private String devPrompt;
        private String userPrompt;

        public OpenAI(String contentInput) {
                this.contentInput = contentInput;

                
        }
        // ESTA LINEA ESTÁ SOLO PARA PROBAR LA API KEY!!!! ESTA HARDCODEADA



        String API_KEY= OpenAIConfig.getApiKey();// Configures using one of:
 
        // - The `OPENAI_API_KEY` environment variable
        // - The `OPENAI_BASE_URL` and `AZURE_OPENAI_KEY` environment variables

        public void start(Consumer<String> onDelta){
                if (contentInput == null || contentInput.trim().isEmpty()) {
                        this.devPrompt = "Responde como un ñero bogotano que lanza chistes pesados con vocabulario grosero, que dice mucho 'ñero' en lugar de 'parcero', 'pirobo' y'gonorrea'";
                        this.userPrompt = "No hay salida de Nmap. Háblame de otra cosa.";
                } else {
                        this.devPrompt = "Eres un analista de redes en ciberseguridad. Interpreta la salida de Nmap con profesionalismo. Responde tanto cualitativa como tecnicamente. No te extiendas tanto";
                        this.userPrompt = "Analiza la siguiente salida de Nmap:\n" + contentInput;
                }

                OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                .apiKey(API_KEY)
                .build();

                ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                        .model(OpenAIConfig.getModelString()) // --> pendiente por si toca cambiarlo a chatmodel de nuevo
                        .maxCompletionTokens(OpenAIConfig.getMaxTokens())
                        .addDeveloperMessage(devPrompt)
                        .addUserMessage(userPrompt+contentInput)
                        .build();

                client.chat()
                        .completions()
                        .createStreaming(createParams)
                        .subscribe(completion -> completion.choices().stream()
                                .flatMap(choice -> choice.delta().content().stream())
                                .forEach(onDelta))
                        .onCompleteFuture()
                        .join();
        }



        public String generateBitacora() {
            this.devPrompt = "Eres un analista experto en ciberseguridad. Desarrollas una bitácora explicita y extendida del contenido que te pasan, de forma que el usuario final pueda entender qué vulnerabilidades tiene su sistema y qué tiene que considerar para mejorar su seguridad";
            this.userPrompt = "Analiza la siguiente bitacora del cliente, donde se muestran los hallazgos y observaciones en total:\n" + contentInput;
            OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                .apiKey(API_KEY)
                .build();

            ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .model(OpenAIConfig.getModelString())
                .maxCompletionTokens(OpenAIConfig.getMaxTokens())
                .addDeveloperMessage(devPrompt)
                .addUserMessage(userPrompt)
                .build();

            // Llamada síncrona (no streaming)
            try {
                var response = client.chat()
                    .completions()
                    .create(createParams)
                    .get(); // Espera la respuesta

                // Obtén el texto de la primera opción
                if (response.choices() != null && !response.choices().isEmpty()) {
                    var choice = response.choices().get(0);
                    if (choice.message() != null && choice.message().content().isPresent()) {
                        return choice.message().content().get();
                    }
                }
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "[Error al generar bitácora IA]";
            }
        }
}