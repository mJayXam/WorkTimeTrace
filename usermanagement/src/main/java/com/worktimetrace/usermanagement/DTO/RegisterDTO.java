package com.worktimetrace.usermanagement.DTO;

public record RegisterDTO(String username, String password, String firstname, String lastname, String street, int housenumber, String zipcode, String city) {
    
}
