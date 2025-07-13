package org.catatunbo.spynet.auditUtils;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.ChatCompletionCreateParams;

import java.util.function.Consumer;

public class CompletionsStreamingAsyncExample {
        
        private String nmapOutput="";

        private String devPrompt;
        private String userPrompt;

        public CompletionsStreamingAsyncExample(String nmapOutput) {
                this.nmapOutput = nmapOutput;

                if (nmapOutput == null || nmapOutput.trim().isEmpty()) {
                        this.devPrompt = "Responde como un ñero bogotano que lanza chistes pesados con vocabulario grosero, que dice mucho 'ñero' en lugar de 'parcero', 'pirobo' y'gonorrea'";
                        this.userPrompt = "No hay salida de Nmap. Háblame de otra cosa.";
                } else {
                        this.devPrompt = "Eres un analista de redes en ciberseguridad. Interpreta la salida de Nmap con profesionalismo.";
                        this.userPrompt = "Analiza la siguiente salida de Nmap:\n" + nmapOutput;
                }
        }
        // ESTA LINEA ESTÁ SOLO PARA PROBAR LA API KEY!!!! ESTA HARDCODEADA



        String API_KEY="";// Configures using one of:
 
        // - The `OPENAI_API_KEY` environment variable
        // - The `OPENAI_BASE_URL` and `AZURE_OPENAI_KEY` environment variables

        public void start(Consumer<String> onDelta){

                OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                .apiKey(API_KEY)
                .build();

                ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_3_5_TURBO)
                        .maxCompletionTokens(256)
                        .addDeveloperMessage(devPrompt)
                        .addUserMessage(userPrompt+nmapOutput)
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
}