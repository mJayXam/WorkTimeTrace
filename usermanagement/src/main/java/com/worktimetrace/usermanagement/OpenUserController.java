package com.worktimetrace.usermanagement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDto) {
    logger.info("REGISTER");
    UserEntity newUser = userService.register(registerDto);
    if (newUser == null) {
      logger.info("Creating user " + registerDto.username() + " failed. Username already exists.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(String.format("Nutzer mit Nutzernamen '%s' existiert bereits.", registerDto.username()));
    }
    logger.info("New user " + registerDto.username() + " created.");
    return ResponseEntity.ok().body(newUser);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO request) {
    logger.info("LOGIN");
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    logger.info("User " + request.username() + " authenticated.");
    String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.username()));
    return ResponseEntity.ok(new LoginResponseDTO(request.username(), token));
  }
}