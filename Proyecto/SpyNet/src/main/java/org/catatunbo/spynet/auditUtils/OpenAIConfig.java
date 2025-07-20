package org.catatunbo.spynet.auditUtils;

import java.io.InputStream;
import java.util.Properties;

public class OpenAIConfig {
    private static final Properties props = new Properties();

    static {
        try {
            InputStream publicInput = OpenAIConfig.class.getClassLoader().getResourceAsStream("openai_resources/openai.properties");
            if (publicInput != null) props.load(publicInput);

            InputStream secretInput = OpenAIConfig.class.getClassLoader().getResourceAsStream("openai_resources/openai.secret.properties");
            if (secretInput != null) props.load(secretInput);

        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración OpenAI: " + e.getMessage(), e);
        }
    }

    public static String getApiKey() {
        String key = props.getProperty("api_key");
        // System.out.println(key);
        if (key == null || key.isBlank()) {
            throw new RuntimeException("API key no configurada");
        }
        return key;
    }

    public static String getModelString() {
        return props.getProperty("model", "gpt-3.5-turbo");
    }

    public static int getMaxTokens() {
        return Integer.parseInt(props.getProperty("max_tokens", "256"));
    }

    public static String getSystemPrompt() {
        return props.getProperty("system_prompt", "Eres un analista de ciberseguridad.");
    }
}
