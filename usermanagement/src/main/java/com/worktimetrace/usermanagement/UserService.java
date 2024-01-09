package com.worktimetrace.usermanagement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.worktimetrace.usermanagement.model.User;
import com.worktimetrace.usermanagement.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);
        user.setSalt(salt);

        return userRepository.save(user);
    }

    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            String hashedPassword = hashPassword(password, user.getSalt());
            return hashedPassword.equals(user.getPasswordHash());
        }

        return false;
    }

    private String generateSalt() {
        // Implement your salt generation logic here
        // For simplicity, you can use a library or a secure random generator
        return "randomSalt";
    }

    private String hashPassword(String password, String salt) {
        // Combine password and salt and then hash using bcrypt
        return passwordEncoder.encode(password + salt);
    }
}

