package com.epam.training.gen.ai.promt;


import com.azure.ai.openai.models.ChatRequestAssistantMessage;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class ChatHistoryService {

    private final List<ChatRequestMessage> chatHistory = new ArrayList<>();

    public List<ChatRequestMessage> getChatHistory() {
        return chatHistory;
    }

    public void addUserMessage(String userMessage) {
        chatHistory.add(new ChatRequestUserMessage(userMessage));
    }
    public void addSystemMessage(String assistantMessage) {
        chatHistory.add(new ChatRequestSystemMessage(assistantMessage));
    }

    public void clearHistory() {
        chatHistory.clear();
    }
}

