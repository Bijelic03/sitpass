package com.ftn.sitpass.service.ues;

import com.ftn.sitpass.exception.BadRequestException;
import com.ftn.sitpass.exception.InternalServerException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PdfTextExtractor {

    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "";
        }
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            return new PDFTextStripper().getText(document);
        } catch (IOException e) {
            throw new BadRequestException("Uploaded PDF is invalid or unreadable.");
        } catch (Exception e) {
            throw new InternalServerException("Unable to parse uploaded PDF.", e);
        }
    }
}
