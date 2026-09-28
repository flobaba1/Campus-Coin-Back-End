package com.campuscoin.backend.service.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    private static final Logger log =
            LoggerFactory.getLogger(AiService.class);

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String testAi() {

        try {
            return chatClient
                    .prompt()
                    .user("Explain what a budget is to a university student.")
                    .call()
                    .content();

        } catch (Exception e) {
            log.error("Gemini request failed", e);
            throw e;
        }

    }

    public String generate(
            String systemPrompt,
            String userPrompt
    ) {
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

    public <T> T generateStructured(
            String systemPrompt,
            String userPrompt,
            Class<T> responseType
    ) {
        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .entity(responseType);
    }
}
