package com.worktimetrace.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class SecurityManger {

    @Value("${usermanagement.url}")
    private static String usermanagementUrl;


    public static ResponseEntity<User> wrongToken(String username, String token){
        RestTemplate rt = new RestTemplate();
        String url = usermanagementUrl + "/auth/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", username);
        headers.set("Authorization","Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<User> responseEntity;
        try{
            responseEntity = rt.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<User>() {});
        }catch(Exception e){
            responseEntity = ResponseEntity.status(401).build();
        }
        return responseEntity;
    }
        
        
}
