package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController

public class PromptController {

    @Autowired
    SimplePromptService promptService;

    /**
     * Endpoint to generate a response based on the provided prompt.
     * <p>
     * The provided prompt is sent to the Azure OpenAI service via the
     * {@link SimplePromptService} to generate a response. The response
     * is returned as a string.
     * </p>
     *
     * @param prompt the text input to be used as a prompt for generating a response
     * @return the generated response as a json
     */
    @GetMapping(value = "/api/generate-response")
    public ResponseEntity<Map<String, Object>> generateResponse(@RequestParam(value = "input") String input) {
        Map<String, Object> response = new HashMap<>();

        try {
            var chats = promptService.getChatCompletions(input);

            response.put("status", "success");
            response.put("data", chats);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error generating response");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
