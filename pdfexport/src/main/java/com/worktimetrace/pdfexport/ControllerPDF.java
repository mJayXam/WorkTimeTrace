package com.worktimetrace.pdfexport;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.worktimetrace.DataTypes.Stunden;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ControllerPDF {
    @GetMapping("bill/{user}/{rate}")
    public void getMethodName(@PathVariable Long user, @PathVariable float rate, HttpServletResponse response) throws DocumentException, IOException {
        ArrayList<Stunden> hourList = getHourList(user);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        PdfPage page = pdf.addNewPage();

        PdfCanvas canvas = new PdfCanvas(page);
        float borderWidth = 2f;
        Color borderColor = ColorConstants.BLACK;

        // Draw a rectangle border around the entire page
        canvas.setStrokeColor(borderColor);
        canvas.setLineWidth(borderWidth);
        canvas.rectangle(borderWidth / 2 + 10, borderWidth / 2 + 10, page.getPageSize().getWidth() - borderWidth - 20, page.getPageSize().getHeight() - borderWidth - 20);
        canvas.stroke();

        // Add your content to the page here
        float yPosition = page.getPageSize().getHeight() - 50;
        canvas.beginText()
                .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12) // Set your font and size
                .setColor(ColorConstants.BLACK, true)
                .moveText(50, yPosition) // Adjust these coordinates as needed
                .showText("Stundenanzahl:")
                .endText();

        yPosition -= 15;
        for (var e : hourList) {
            canvas.beginText()
                    .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12) // Set your font and size
                    .setColor(ColorConstants.BLACK, true)
                    .moveText(50, yPosition)
                    .showText(e.getStundenanzahl().toString())
                    .endText();
            yPosition -= 15;
        }

        canvas.endText();

        pdf.close();

        byte[] pdfBytes = baos.toByteArray();

        response.setContentType("application/pdf");
        response.setContentLength(pdfBytes.length);
        response.setHeader("Content-Disposition", "attachment; filename=example.pdf");

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    private ArrayList<Stunden> getHourList(Long user) {
        RestTemplate rt = new RestTemplate();
        String url = "http://timemanagement:8080/byNID/"+user;

        ResponseEntity<ArrayList<Stunden>> responseEntity = rt.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ArrayList<Stunden>>() {});

        ArrayList<Stunden> hourList = responseEntity.getBody();
        return hourList;
    }
    
}
