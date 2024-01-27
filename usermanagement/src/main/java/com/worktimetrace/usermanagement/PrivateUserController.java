package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class PrivateUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUsername;
        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(currentUsername);
        if (userInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfoDTO);
    }
}