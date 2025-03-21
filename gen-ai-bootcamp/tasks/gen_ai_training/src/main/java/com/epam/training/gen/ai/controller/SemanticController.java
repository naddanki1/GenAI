package com.epam.training.gen.ai.controller;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class SemanticController {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;

    @GetMapping(value = "/api/kernel/generate-response")
    public ResponseEntity<Map<String, Object>> getAiResponse(@RequestParam(value = "input") String input) {
        Map<String, Object> res = new HashMap<>();
        try {
            var chats = chatCompletionService.getChatMessageContentsAsync(input, kernel, null)
                    .map(response -> response.stream()
                            .filter(message -> message.getAuthorRole() == AuthorRole.ASSISTANT)
                            .map(ChatMessageContent::getContent)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse("No completion content received from OpenAI")
                    )
                    .block();
            res.put("status", "success");
            res.put("data", chats);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error generating response");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
