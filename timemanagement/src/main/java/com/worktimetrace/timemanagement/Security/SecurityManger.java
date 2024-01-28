package com.worktimetrace.timemanagement.Security;

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


    public static ResponseEntity<User> wrongToken(String username, String token){
        RestTemplate rt = new RestTemplate();
        String url = "http://usermanagement:8080/user/info";
        // String url = "https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/user/info";
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
