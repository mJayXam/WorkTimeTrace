package com.worktimetrace.pdfexport;

import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ControllerPDF {
    @GetMapping("bill/{user}/{rate}")
    public void getMethodName(@PathVariable Long user, @PathVariable float rate, HttpServletResponse response) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        document.add(new Paragraph("Hello, this is a PDF content."));
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        response.setContentType("application/pdf");
        response.setContentLength(pdfBytes.length);
        response.setHeader("Content-Disposition", "attachment; filename=example.pdf");

        // Schreibe die PDF-Daten direkt in den Ausgabestrom der HTTP-Antwort
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
    
}
