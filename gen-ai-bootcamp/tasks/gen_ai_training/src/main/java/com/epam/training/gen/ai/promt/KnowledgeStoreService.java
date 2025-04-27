package com.epam.training.gen.ai.promt;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class KnowledgeStoreService {

    private final Map<String, String> knowledgeStore = new HashMap<>();

    // Adds extracted knowledge to the store
    public String addKnowledge(String content) {
        String uniqueId = UUID.randomUUID().toString();
        knowledgeStore.put(uniqueId, content);
        return uniqueId;
    }

    // Fetches relevant knowledge based on the provided query
    public String getRelevantKnowledge(String query) {
        return knowledgeStore.entrySet().stream()
                .filter(entry -> entry.getValue().toLowerCase().contains(query.toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("No relevant knowledge found.");
    }
}
