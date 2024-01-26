package com.worktimetrace.timemanagement.Security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class SecurityManger {


    public static boolean wrongToken(Token tok){
        String url = "http://usermanagement/auth/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(tok.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String val = restTemplate.postForObject(url, requestEntity, String.class);
        if(val != null && val.equals("Token is valid"))
            return false;
        return true;
    }
        
}
