package me.mppombo.synchronyapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/*
 * The JSON object sent in a POST request when registering a new user.
 * Requires:
 * - username
 * - password
 * - email
 * And optionally:
 * - firstName
 * - lastName
 */
public record RegisterDto(
        @NotBlank
        @Size(min = 2, message = "Username must have at least 2 characters")
        String username,

        @NotEmpty
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password,

        @NotEmpty
        @Email
        String email,

        String firstName,
        String lastName) {
    @Override
    public String toString() {
        // Just don't wanna be logging passwords.
        return String.format("NewUser[username='%s', email='%s', firstName='%s', lastName='%s']",
                username, email, firstName, lastName);
    }
}
