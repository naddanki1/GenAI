package com.epam.training.gen.ai.strategy;

import com.epam.training.gen.ai.Constants;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import org.springframework.stereotype.Component;

@Component
public class ChatCompletionStrategy {

    public PromptExecutionSettings getDefaultSettings() {
        return PromptExecutionSettings.builder()
                .withTemperature(Constants.TEMPERATURE)
                .withMaxTokens(Constants.MAX_TOKENS)
                .build();
    }
}
