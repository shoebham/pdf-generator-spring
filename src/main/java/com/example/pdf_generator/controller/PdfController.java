package com.example.pdf_generator.controller;


import com.example.pdf_generator.model.Invoice;
import com.example.pdf_generator.service.PdfGeneratorService;
import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<FileSystemResource> generatePdf(@Valid @RequestBody Invoice invoice) {
        File pdf = pdfGeneratorService.generatePdf(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(pdf));
    }
}