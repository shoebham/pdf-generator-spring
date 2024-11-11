package com.example.pdf_generator.service;


import com.example.pdf_generator.model.Invoice;
import com.example.pdf_generator.model.Item;
import com.example.pdf_generator.service.PdfGeneratorService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;
@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGeneratorServiceImpl.class);

    @Value("${pdf.storage.path}")
    private String storagePath;

    @Override
    public File generatePdf(Invoice invoice) {
        String hash = generateHash(invoice);
        String fileName = hash + ".pdf";
        Path filePath = Path.of(storagePath, fileName);
        // Check if PDF already exists
        File existingPdf = getPdfIfExists(invoice,hash,filePath);
        if (existingPdf != null) {
            logger.info("Pdf already exists, skipping generation");
            return existingPdf;
        }

        try {
            Files.createDirectories(Path.of(storagePath));
            PdfWriter writer = new PdfWriter(filePath.toString());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // Main table to hold everything with border
            Table mainTable = new Table(1);
            mainTable.setWidth(UnitValue.createPercentValue(100));
            mainTable.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            // Create header table with two columns
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Seller information (left column)
            Cell sellerCell = new Cell();
            sellerCell.add(new Paragraph("Seller:").setBold());
            sellerCell.add(new Paragraph(invoice.getSeller()));
            sellerCell.add(new Paragraph(invoice.getSellerAddress()));
            sellerCell.add(new Paragraph("GSTIN: " + invoice.getSellerGstin()));
            sellerCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            sellerCell.setPadding(10);
            headerTable.addCell(sellerCell);

            // Buyer information (right column)
            Cell buyerCell = new Cell();
            buyerCell.add(new Paragraph("Buyer:").setBold());
            buyerCell.add(new Paragraph(invoice.getBuyer()));
            buyerCell.add(new Paragraph(invoice.getBuyerAddress()));
            buyerCell.add(new Paragraph("GSTIN: " + invoice.getBuyerGstin()));
            buyerCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            buyerCell.setPadding(10);
            headerTable.addCell(buyerCell);

            // Add header table to main table
            Cell headerContainer = new Cell().add(headerTable);
            headerContainer.setPadding(0);
            headerContainer.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            mainTable.addCell(headerContainer);

            // Create items table
            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{40, 20, 20, 20}));
            itemsTable.setWidth(UnitValue.createPercentValue(100));

            // Add table headers
            String[] headers = {"Item", "Quantity", "Rate", "Amount"};
            for (String header : headers) {
                Cell cell = new Cell()
                        .add(new Paragraph(header))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5);
                itemsTable.addHeaderCell(cell);
            }

            // Add items
            for (Item item : invoice.getItems()) {
                itemsTable.addCell(new Cell().add(new Paragraph(item.getName()))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));
                itemsTable.addCell(new Cell().add(new Paragraph(item.getQuantity()))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));
                itemsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getRate())))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));
                itemsTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", item.getAmount())))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));
            }

            // Add items table to main table
            Cell itemsContainer = new Cell().add(itemsTable);
            itemsContainer.setPadding(0);
            itemsContainer.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
            mainTable.addCell(itemsContainer);

            document.add(mainTable);
            document.close();

            return filePath.toFile();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    @Override
    public File getPdfIfExists(Invoice invoice,String hash,Path filePath) {
        File file = filePath.toFile();
        return file.exists() ? file : null;
    }

    @Override
    public String generateHash(Invoice invoice) {
        try {
            String content = invoice.getSeller() + invoice.getBuyer() +
                    invoice.getItems().toString(); // Add more fields as needed
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes());
            return Base64.getUrlEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate hash", e);
        }
    }
}
