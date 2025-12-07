package com.itsutra.ai.project.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenAIClient {

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model}")
    private String defaultModel;

    @Value("${ai.openai.temperature}")
    private Double temperature;

    @Value("${ai.openai.max-tokens}")
    private Integer maxTokens;

    private OpenAiService openAiService;

    private OpenAiService getService() {
        if (openAiService == null) {
            openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        }
        return openAiService;
    }

    public String callChatGPT(String prompt, String model) {
        try {
            ChatMessage message = new ChatMessage("user", prompt);

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model != null ? model : defaultModel)
                    .messages(Arrays.asList(message))
                    .temperature(temperature)
                    .maxTokens(maxTokens)
                    .build();

            return getService().createChatCompletion(request)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

        } catch (Exception e) {
            log.error("Error calling OpenAI API: {}", e.getMessage(), e);
            throw new RuntimeException("OpenAI API call failed", e);
        }
    }

    public List<Double> createEmbedding(String text) {
        try {
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .model("text-embedding-ada-002")
                    .input(Arrays.asList(text))
                    .build();

            EmbeddingResult result = getService().createEmbeddings(request);
            return result.getData().get(0).getEmbedding();

        } catch (Exception e) {
            log.error("Error creating embedding: {}", e.getMessage(), e);
            throw new RuntimeException("Embedding creation failed", e);
        }
    }

    public void testConnection() {
        try {
            getService().listModels();   // <-- correct method
            log.info("OpenAI connection test successful");
        } catch (Exception e) {
            log.error("OpenAI connection test failed: {}", e.getMessage());
            throw e;
        }
    }

}
