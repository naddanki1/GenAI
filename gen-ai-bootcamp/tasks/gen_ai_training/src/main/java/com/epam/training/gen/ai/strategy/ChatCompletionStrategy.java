package com.epam.training.gen.ai.strategy;

import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatCompletionStrategy {
    @Value("${ai.model.temperature}")
    Double temperature;

    @Value("${ai.model.max-tokens}")
    Integer maxTokens;

    public PromptExecutionSettings getDefaultSettings() {
        return PromptExecutionSettings.builder()
                .withTemperature(temperature)
                .withMaxTokens(maxTokens)
                .build();
    }
}
