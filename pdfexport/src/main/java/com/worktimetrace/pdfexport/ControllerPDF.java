package com.worktimetrace.pdfexport;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.worktimetrace.DataTypes.Bill;
import com.worktimetrace.DataTypes.Hours;
import com.worktimetrace.pdfexport.Security.*;
import com.worktimetrace.pdfexport.Security.SecurityManager;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class ControllerPDF {


    @Value("${timemanagement.url}")
    private static String timemanagementUrl;

        @Autowired
        SecurityManager sec;

        Logger logger = LoggerFactory.getLogger(getClass());

        @GetMapping("bill/{rate}")
        public void getBill(@PathVariable float rate, HttpServletResponse response,
                        @RequestHeader("username") String username,
                        @RequestHeader("Authorization") String token)
                        throws DocumentException, IOException {

                logger.info("GET BILL");
                var UserResp = sec.wrongToken(username, token.substring("Bearer ".length()));
                if (!UserResp.getStatusCode()
                                .is2xxSuccessful()) {
                        response.setStatus(401);
                        ;
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
                logger.info("getBill OK");
        }

        private ArrayList<Hours> getHourList(User user, String token) {
                RestTemplate rt = new RestTemplate();
                String url = timemanagementUrl + "/byNID/" + user.getId();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("username", user.getUsername());
                headers.set("Authorization", "Bearer " + token);

                HttpEntity<String> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<ArrayList<Hours>> responseEntity;
                try {
                        responseEntity = rt.exchange(
                                        url,
                                        HttpMethod.GET,
                                        requestEntity,
                                        new ParameterizedTypeReference<ArrayList<Hours>>() {
                                        });
                } catch (Exception e) {
                        responseEntity = ResponseEntity.status(401).build();
                }
                ArrayList<Hours> hourList = responseEntity.getBody();
                if (hourList == null)
                        return new ArrayList<Hours>();
                return hourList;
        }
}
