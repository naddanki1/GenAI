package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.promt.PDFUploadService;
import com.epam.training.gen.ai.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rag")
public class RagController {
    @Autowired
    private SimplePromptService simplePromptService; // Use SimplePromptService to handle queries and OpenAI integration

    @Autowired
    private PDFUploadService pdfUploadService;
    /**
     * Query the knowledge base and generate a response from OpenAI using a single request parameter.
     *
     * @param query the user's input query (single parameter)
     * @return the generated response from OpenAI
     */
    @GetMapping("/search")
    public String queryKnowledge(@RequestParam("query") String query,@RequestParam(value="model", required = false) String model) {
        // Fetch relevant knowledge and generate a response from OpenAI
        List<String> response = simplePromptService.getChatCompletions(query,model);

        // Return the generated response (or fallback message if no response)
        return response.isEmpty() ? "No response generated." : response.get(0);
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPDF(@RequestParam("file") MultipartFile file) {
        try {
            // Handle the PDF upload and store the extracted content in the knowledge store
            String knowledgeId = pdfUploadService.handlePDFUpload(file);
            return ResponseEntity.ok("PDF uploaded successfully. Knowledge stored with ID: " + knowledgeId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error extracting text from PDF: " + e.getMessage());
        }
    }
}
