package com.worktimetrace.usermanagement.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank(message = "Nutzername darf nicht leer sein")
    String username,

    @NotBlank(message = "Passwort darf nicht leer sein")
    @Size(min = 6, max = 20, message = "Passwort muss zwischen 6 und 20 Zeichen lang sein")
    String password) {
}
