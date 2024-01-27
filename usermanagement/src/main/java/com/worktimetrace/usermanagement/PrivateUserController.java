package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.service.UserDetailsServiceImpl;
import com.worktimetrace.usermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class PrivateUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(@RequestHeader("username") String username) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername; // username from JWT
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }

        if (!username.equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (!userService.validate(currentUsername, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(username);
        if (userInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfoDTO);
    }

}