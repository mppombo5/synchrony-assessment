package me.mppombo.synchronyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public record ApiUserCreateDto(
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
        String lastName) { }
