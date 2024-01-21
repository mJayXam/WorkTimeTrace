package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(username);
        if (userInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfoDTO);
    }
}