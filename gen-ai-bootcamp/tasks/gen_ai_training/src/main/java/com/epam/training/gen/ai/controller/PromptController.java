package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
     * @return the generated response as a string
     */
    @GetMapping(value = "/api/generate-response")
    public ResponseEntity<String> generateResponse(@RequestParam(value = "prompt") String prompt) throws IOException, InterruptedException {
        try {
            var chats = promptService.getChatCompletions(prompt);
            return ResponseEntity.ok(chats.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating response");
        }

    }

}
