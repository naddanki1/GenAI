package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.AgeCalculatorPlugin;
import com.epam.training.gen.ai.plugin.CurrencyConverterPlugin;
import com.epam.training.gen.ai.plugin.LightPlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SemanticKernelConfiguration {

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient client, @Value("${client-azureopenai-deployment-name:gpt-4}") String deploymentModel)  {
        return OpenAIChatCompletion.builder()
            .withOpenAIAsyncClient(client)
            .withModelId(deploymentModel)
            .build();
    }

    @Bean
    public Kernel kernel(ChatCompletionService openAIChatCompletion) {
        KernelPlugin lightPlugin = KernelPluginFactory.createFromObject(new LightPlugin(), "LightPlugin");
        KernelPlugin ageCalculatorPlugin = KernelPluginFactory.createFromObject(new AgeCalculatorPlugin(), "AgeCalculatorPlugin");
        KernelPlugin currencyConverterPlugin = KernelPluginFactory.createFromObject(new CurrencyConverterPlugin(), "CurrencyConverterPlugin");

        return Kernel.builder()
                .withAIService(ChatCompletionService.class, openAIChatCompletion)
                .withPlugin(lightPlugin)
                .withPlugin(ageCalculatorPlugin)
                .withPlugin(currencyConverterPlugin)
                .build();
    }
}
