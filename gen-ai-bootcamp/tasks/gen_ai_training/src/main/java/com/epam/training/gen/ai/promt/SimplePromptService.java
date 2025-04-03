package com.epam.training.gen.ai.promt;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ImageGenerationOptions;
import com.azure.ai.openai.models.ImageGenerations;
import com.azure.ai.openai.models.ImageSize;
import com.epam.training.gen.ai.configuration.ModelConfig;
import com.epam.training.gen.ai.strategy.ChatCompletionStrategy;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on a static greeting message. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@Service
public class SimplePromptService {

    private final OpenAIAsyncClient aiAsyncClient;
    private final Map<String, String> textModels;
    private final Map<String,String> imageModels;
    private final ChatCompletionStrategy chatCompletionStrategy;
    private final Kernel kernel;
    private final ChatCompletionService chatCompletionService;
    @Autowired
    ChatHistory chatHistory;
    @Value("${client-azureopenai-key}")
    private String apiKey;

    public SimplePromptService(OpenAIAsyncClient aiAsyncClient,
                               ModelConfig modelConfig, Map<String, String> textModels, Map<String, String> imageModels,
                               ChatCompletionStrategy chatCompletionStrategy,
                               Kernel kernel, ChatCompletionService chatCompletionService) {
        this.aiAsyncClient = aiAsyncClient;
        this.textModels = modelConfig.getTextModels();
        this.imageModels = modelConfig.getImageModels();
        this.chatCompletionStrategy = chatCompletionStrategy;
        this.kernel = kernel;
        this.chatCompletionService = chatCompletionService;
    }

    /**
     * Generates chat completions for a given prompt.
     * <p>
     * This method sends the provided prompt to the Azure OpenAI service,
     * waits for the response asynchronously, and returns a list of response messages.
     * </p>
     *
     * @param prompt the text input that serves as the prompt for generating the response
     * @param model  key
     * @return a list of response messages generated by the OpenAI model
     */
    public List<String> getChatCompletions(String prompt, String model) {
        String deploymentOrModelName = "";
        chatHistory.addUserMessage(prompt);
        if(imageModels.containsKey(model)){
            deploymentOrModelName = imageModels.get(model);
            return generateImage(prompt, deploymentOrModelName);
        }
        else {
            deploymentOrModelName = textModels.get(model);
            return generateTextResponse(prompt, deploymentOrModelName);
        }
    }

    private List<String> generateTextResponse(String prompt, String model) {
        ChatCompletionService chatCompletionService = buildChatCompletionService(model);
        List<ChatMessageContent<?>> response = fetchChatResponse(chatCompletionService, chatHistory,
                kernel);
        List<String> chats = new ArrayList<>();

        if (!CollectionUtils.isEmpty(response)) {
            log.info(response.get(0).getContent());
            response.stream()
                    .filter(res -> res.getContent() !=null)
                            .forEach(res -> chatHistory.addSystemMessage(res.getContent()));
            chats = response.stream()
                    .map(ChatMessageContent::getContent)
                    .collect(Collectors.toList());
        }

        return chats;
    }

    private ChatCompletionService buildChatCompletionService(String model) {
        return OpenAIChatCompletion.builder()
                .withOpenAIAsyncClient(aiAsyncClient)
                .withModelId(model)
                .build();
    }

    private List<ChatMessageContent<?>> fetchChatResponse(
            ChatCompletionService chatCompletionService,
            ChatHistory history,
            Kernel kernel
    ) {
        InvocationContext optionalInvocationContext = InvocationContext.builder()
                .withPromptExecutionSettings(chatCompletionStrategy.getDefaultSettings())
        .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true)).build();
        return chatCompletionService.getChatMessageContentsAsync(history, kernel,
                optionalInvocationContext).block();
    }

    public List<String> generateImage(String prompt, String model) {

        ImageGenerationOptions options = new ImageGenerationOptions(model)
                .setN(1) // Number of images to generate
                .setSize(ImageSize.SIZE1024X1024); // Image resolution

        ImageGenerations imageGenerations = aiAsyncClient.getImageGenerations(prompt, options).block();

        List<String> urls = imageGenerations.getData().stream()
                .map(image -> image.getUrl())
                .collect(Collectors.toList());
        System.out.println("Urls");
        return urls;
    }


}
