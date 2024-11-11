package com.example.pdf_generator.service;

import com.example.pdf_generator.model.Invoice;
import java.io.File;
import java.nio.file.Path;

public interface PdfGeneratorService {
    File generatePdf(Invoice invoice);

    File getPdfIfExists(Invoice invoice, String hash, Path filePath);

    String generateHash(Invoice invoice);
}
