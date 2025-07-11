package org.catatunbo.spynet.auditUtils;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.ChatCompletionCreateParams;


public class CompletionsStreamingAsyncExample {
        
        private String nmapOutput="";

        public CompletionsStreamingAsyncExample(String nmapOutput) {
                this.nmapOutput = nmapOutput;
        }
        // ESTA LINEA ESTÁ SOLO PARA PROBAR LA API KEY!!!! ESTA HARDCODEADA
        String API_KEY="aquí solía haber una api key";
        // Configures using one of:
        // - The `OPENAI_API_KEY` environment variable
        // - The `OPENAI_BASE_URL` and `AZURE_OPENAI_KEY` environment variables

        void start(){

                OpenAIClientAsync client = OpenAIOkHttpClientAsync.builder()
                .apiKey(API_KEY)
                .build();

                ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_3_5_TURBO)
                        .maxCompletionTokens(256)
                        .addDeveloperMessage("Responde como si fueras un analista de redes en ciberseguridad. Asegurate de sonar experto y amable")
                        .addUserMessage("Analiza la respuesta de esta salida de nmap: "+nmapOutput)
                        .build();

                client.chat()
                        .completions()
                        .createStreaming(createParams)
                        .subscribe(completion -> completion.choices().stream()
                                .flatMap(choice -> choice.delta().content().stream())
                                .forEach(System.out::print))
                        .onCompleteFuture()
                        .join();

        }
}