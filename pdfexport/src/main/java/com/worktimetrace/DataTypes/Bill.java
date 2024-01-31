package com.worktimetrace.DataTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.worktimetrace.pdfexport.Security.User;

public class Bill {
    ByteArrayOutputStream baos;
    PdfWriter writer;
    PdfDocument pdf;
    PdfDocument basicbill;
    PdfPage page;
    PdfCanvas canvas;
    float borderWidth;
    Color borderColor;

    Bill() throws IOException {

    }

    public static Bill make() throws IOException {
        var ret =  new Bill();
        ret.baos = new ByteArrayOutputStream();
        ret.writer = new PdfWriter(ret.baos);
        ret.pdf = new PdfDocument(ret.writer);
        ret.basicbill = new PdfDocument(new PdfReader("./BasicBill.pdf"));
        ret.basicbill.copyPagesTo(1, 1, ret.pdf);
        ret.page = ret.pdf.getFirstPage();
        ret.canvas = new PdfCanvas(ret.page);
        ret.borderWidth = 2f;
        ret.borderColor = ColorConstants.BLACK;
        ret.canvas.concatMatrix(1, 0, 0, -1, 0, ret.page.getPageSize().getHeight());
        return ret;
    }

    public byte[] build(){
        pdf.close();
        byte[] pdfBytes = baos.toByteArray();
        return pdfBytes;
    }

    public Bill addBorder() {
        canvas.setStrokeColor(borderColor);
        canvas.setLineWidth(borderWidth);
        canvas.rectangle(borderWidth / 2 + 10, borderWidth / 2 + 10, page.getPageSize().getWidth() - borderWidth - 20,
                page.getPageSize().getHeight() - borderWidth - 20);
        canvas.stroke();
        return this;
    }

    public Bill addHourCounter(ArrayList<Hours> hourList, float x, float y) throws IOException {
        var yPosHour = y;
        var xPosHour = x;

        yPosHour -= 15;
        for (var e : hourList) {
            canvas.beginText()
                    .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12)
                    .setColor(ColorConstants.BLACK, true)
                    .moveText(xPosHour + 20, yPosHour)
                    .showText(e.getHourcount().toString() + " (" + e.getHourdate().toString() + ")" + " +")
                    .endText();
            yPosHour -= 15;
        }

        canvas.setStrokeColor(borderColor);
        canvas.setLineWidth(1f);
        canvas.moveTo(xPosHour, yPosHour + 12);
        canvas.lineTo(xPosHour + 150, yPosHour + 12);
        canvas.stroke();

        canvas.beginText()
                .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12)
                .setColor(ColorConstants.BLACK, true)
                .moveText(xPosHour + 20, yPosHour)
                .showText(hourList.stream().mapToDouble(Hours::getHourcount).sum() + "")
                .endText();
        return this;
    }

    public Bill addQuota(ArrayList<Hours> hourList,float rate, float x, float y) throws IOException{
        var yPos = y;
        var xPos = x;

        String rateS = "Errechnete Gesamtkosten nach Satz: " + rate + "€ / std: ";
        String quotaS = "" + hourList.stream().mapToDouble(Hours::getHourcount).sum() + " * " + rate + " € / std"
                + " = " +
                hourList.stream().mapToDouble(Hours::getHourcount).sum() * rate;

        canvas.beginText()
                .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12) // Set your font and size
                .setColor(ColorConstants.BLACK, true)
                .moveText(xPos, yPos) // Adjust these coordinates as needed
                .showText(rateS)
                .endText();

        yPos -= 15;

        canvas.beginText()
                .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12) // Set your font and size
                .setColor(ColorConstants.BLACK, true)
                .moveText(xPos, yPos) // Adjust these coordinates as needed
                .showText(quotaS)
                .endText();

        return this;
    }

    public Bill addPerson(User user, float y, float x) throws IOException{
        float xPos = x + 10;
        float yPos = y - 10;

        canvas
                .setFontAndSize(com.itextpdf.kernel.font.PdfFontFactory.createFont(), 12) // Set your font and size
                .setColor(ColorConstants.BLACK, true);

        canvas.beginText()
                .moveText(xPos, yPos)
                .showText(user.getFirstname() + " " + user.getLastname())
                .endText();

        yPos -= 15;

        canvas.beginText()
                .moveText(xPos, yPos)
                .showText(user.getStreet() + " Nr. " + user.getHousenumber())
                .endText();

        yPos -= 15;

        canvas.beginText()
                .moveText(xPos, yPos)
                .showText(user.getZipcode() + " " + user.getCity())
                .endText();

        yPos -= 15;

        canvas.setStrokeColor(ColorConstants.BLACK);
        canvas.setLineWidth(1f);
        canvas.rectangle(x, yPos, 150, -(yPos - y) + 15);
        canvas.stroke();

        return this;
    }

}
