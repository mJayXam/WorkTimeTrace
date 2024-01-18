package com.worktimetrace.usermanagement;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.usermanagement.DTO.LoginDTO;
import com.worktimetrace.usermanagement.DTO.RegisterDTO;
import com.worktimetrace.usermanagement.DTO.UserInfoDTO;
import com.worktimetrace.usermanagement.Entity.RoleEntity;
import com.worktimetrace.usermanagement.Entity.UserEntity;
import com.worktimetrace.usermanagement.Repository.RoleRepository;
import com.worktimetrace.usermanagement.Repository.UserRepository;
import com.worktimetrace.usermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(),
                        loginDto.password()));
        System.out.println("Hier komme ich nicht hin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfull!", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDto) {
        if (userRepository.existsByUsername(registerDto.username())) {
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.username());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setFirstname(registerDto.firstname());
        user.setLastname(registerDto.lastname());
        user.setStreet(registerDto.street());
        user.setHousenumber(registerDto.housenumber());
        user.setZipcode(registerDto.zipcode());
        user.setCity(registerDto.city());
        RoleEntity roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDTO> getUserInfoByUsername(@PathVariable String username) {
        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(username);
        if (userInfoDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfoDTO);
    }
}