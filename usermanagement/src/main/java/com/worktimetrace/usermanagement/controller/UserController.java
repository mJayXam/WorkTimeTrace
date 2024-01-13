package com.worktimetrace.usermanagement.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/register")
    public String createUser() {
        try {
            // Testdaten
            String username = "TestUser";
            String passwordSalt = "TestSalt";
            String passwordHash = "TestHash";

            // SQL-Statement zum Einfügen der Daten in die Tabelle
            String sql = "INSERT INTO users (username, password_salt, password_hash) VALUES (?, ?, ?)";

            // Daten in die Tabelle einfügen
            jdbcTemplate.update(sql, username, passwordSalt, passwordHash);

            return "Benutzer erfolgreich erstellt!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Erstellen des Benutzers: " + e.getMessage();
        }
    }
}