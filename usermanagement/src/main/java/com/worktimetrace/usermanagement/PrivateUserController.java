package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class PrivateUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDTO> getUserInfoByUsername(@PathVariable String username) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUsername;
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }

        if (username.equals(currentUsername)) {
            UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(username);
            if (userInfoDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userInfoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}