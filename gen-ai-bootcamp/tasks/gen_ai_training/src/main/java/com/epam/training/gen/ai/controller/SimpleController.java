package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController

public class SimpleController {

    @Autowired
    SimplePromptService promptService;

    @GetMapping(value = "/api/prompt")
    public String createImage(@RequestParam(value = "promtName") String promtName) throws IOException, InterruptedException {
       var chats = promptService.getChatCompletions(promtName);
        return chats.toString();

    }

}
