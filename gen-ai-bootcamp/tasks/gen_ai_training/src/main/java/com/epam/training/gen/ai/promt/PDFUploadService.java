package com.epam.training.gen.ai.promt;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PDFUploadService {

    @Autowired
    private KnowledgeStoreService knowledgeStoreService; // Inject the KnowledgeStoreService

    // Method to handle PDF upload and extract text content
    public String handlePDFUpload(MultipartFile file) throws IOException {
        // Convert the PDF file into text
        String content = extractTextFromPDF(file);

        // Store the extracted text in the knowledge store
        return knowledgeStoreService.addKnowledge(content);
    }

    // Method to extract text from PDF
    private String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
