package com.worktimetrace.usermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.worktimetrace.usermanagement.UserService;
import com.worktimetrace.usermanagement.model.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        User user = userService.registerUser(username, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean isValid = userService.validateUser(username, password);

        if (isValid) {
            // Implement token generation and return it in the response
            String token = generateToken(username);
            return ResponseEntity.ok("Login successful. Token: " + token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        // Implement logout logic (if needed)
        return ResponseEntity.ok("Logout successful");
    }

    private String generateToken(String username) {
        // Implement token generation logic here (e.g., using JWT)
        return "sampleToken";
    }
}
