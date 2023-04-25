package me.mppombo.synchronyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.mppombo.synchronyapi.dto.ApiUserDto;
import me.mppombo.synchronyapi.dto.RegisterDto;

/*
 * Represents a registered user of the API. Contains "basic information", username, and password.
 * "Basic information" includes:
 * - email
 * - first name
 * - last name
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApiUser {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @JsonIgnore
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    private String role;

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', email='%s', firstName='%s', lastName='%s']",
                id, username, email, firstName, lastName);
    }

    public static ApiUser fromRegisterDto(RegisterDto dto) {
        // the order of these items is VERY important
        return new ApiUser(0L, dto.username(), dto.password(), dto.email(), dto.firstName(), dto.lastName(), "USER");
    }

    public ApiUserDto toDto() {
        return new ApiUserDto(id, username, email, firstName, lastName);
    }
}
