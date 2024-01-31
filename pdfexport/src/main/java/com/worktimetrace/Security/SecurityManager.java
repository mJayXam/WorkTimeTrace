package com.worktimetrace.Security;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;




@Service
public class SecurityManager {


    private String usermanagementUrl;

    public SecurityManager(@Value("${usermanagement.url}") String usermanagementUrl) {
        this.usermanagementUrl = usermanagementUrl;
    }

    public void setUsermanagementUrl(String usermanagementUrl) {
        this.usermanagementUrl = usermanagementUrl;
    }

    public ResponseEntity<User> wrongToken(String username, String token){
        RestTemplate rt = new RestTemplate();
        URI url = URI.create(usermanagementUrl + "/user/info");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + token);
        headers.add("username", username);
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
