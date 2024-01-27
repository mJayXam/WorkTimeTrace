package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.service.UserDetailsServiceImpl;
import com.worktimetrace.usermanagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class PrivateUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(HttpServletRequest request) {

        String headerUsername = request.getHeader("Username");

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(headerUsername);
       
        if (userInfoDTO == null) {
            // Wenn Nutzer im Header nicht exisitiert
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(headerUsername);

        if (!userService.validate(request.getHeader("Authorization"), userDetails)) {
            // Wenn JWT in Kombination mit Nutzer nicht gültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userInfoDTO);
    }

}