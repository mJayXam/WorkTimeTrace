package com.worktimetrace.usermanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.usermanagement.DTO.LoginDTO;
import com.worktimetrace.usermanagement.DTO.LoginResponseDTO;
import com.worktimetrace.usermanagement.DTO.RegisterDTO;
import com.worktimetrace.usermanagement.Entity.UserEntity;
import com.worktimetrace.usermanagement.service.JwtService;
import com.worktimetrace.usermanagement.service.UserDetailsServiceImpl;
import com.worktimetrace.usermanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class OpenUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    /*
     * @PostMapping("/login")
     * public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO
     * loginDto) {
     * Authentication authentication = authenticationManager
     * .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(),
     * loginDto.password()));
     * SecurityContextHolder.getContext().setAuthentication(authentication);
     * return new ResponseEntity<>("User login successfull!", HttpStatus.OK);
     * }
     */

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDto) {
        UserEntity newUser = userService.register(registerDto);
        if (newUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(String.format("Nutzer mit Nutzernamen '%s' existiert bereits.", registerDto.username()));
        }
        return ResponseEntity.ok().body(newUser);
    }

    /*
     * @PostMapping("/login")
     * public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO
     * loginDto) {
     * userService.authenticateUser(loginDto);
     * return new ResponseEntity<>("User login successfull!", HttpStatus.OK);
     * }
     */

    /* @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
            @RequestParam("password") String password) {
        try {
            userService.authenticateUser(username, password);
            return ResponseEntity.ok("Login erfolgreich!");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültiger Benutzername oder Passwort.");
        }
    } */

    @PostMapping(value = "/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.username()));
    return ResponseEntity.ok(new LoginResponseDTO(request.username(), token));
  }

}