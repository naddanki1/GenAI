package com.epam.training.gen.ai.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "client-azureopenai-deployments")
public class ModelConfig {
    private Map<String, String> models;
}
