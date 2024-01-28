package com.worktimetrace.pdfexport;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.worktimetrace.DataTypes.Bill;
import com.worktimetrace.DataTypes.Hours;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.worktimetrace.Security.*;

@RestController
public class ControllerPDF {
        @GetMapping("bill/{rate}")
        public void getMethodName(@PathVariable float rate, HttpServletResponse response,
                        @RequestHeader("username") String username,
                        @RequestHeader("Authorization") String token)
                        throws DocumentException, IOException {
                var UserResp = SecurityManger.wrongToken(username, token.substring("Bearer ".length()));
                if (!UserResp.getStatusCode()
                                .is2xxSuccessful()) {
                        response.setStatus(401);;
                        return;
                }
                ArrayList<Hours> hourList = getHourList(UserResp.getBody(), token.substring(("Bearer ").length()));

                User userObj = UserResp.getBody();

                byte[] pdfBytes = Bill
                                .make()
                                .addHourCounter(hourList, 90, 500)
                                .addQuota(hourList, rate, 180, 100)
                                .addPerson(userObj, 700, 30)
                                .addBorder()
                                .build();

                response.setContentType("application/pdf");
                response.setContentLength(pdfBytes.length);
                response.setHeader("Content-Disposition", "attachment; filename=example.pdf");

                response.getOutputStream().write(pdfBytes);
                response.getOutputStream().flush();
        }

        private ArrayList<Hours> getHourList(User user, String token) {
                RestTemplate rt = new RestTemplate();
                String url = "http://timemanagement:8080/byNID/" + user.getId();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("username", user.getUsername());
                headers.set("Authorization","Bearer " + token);

                HttpEntity<String> requestEntity = new HttpEntity<>(headers);
                
                ResponseEntity<ArrayList<Hours>> responseEntity;
                try{
                        responseEntity = rt.exchange(
                            url,
                            HttpMethod.GET,
                            requestEntity,
                            new ParameterizedTypeReference<ArrayList<Hours>>() {});
                    }catch(Exception e){
                        responseEntity = ResponseEntity.status(401).build();
                    }
                ArrayList<Hours> hourList = responseEntity.getBody();
                return hourList;
        }
}
