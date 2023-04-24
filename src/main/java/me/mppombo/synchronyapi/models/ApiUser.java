package me.mppombo.synchronyapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import me.mppombo.synchronyapi.dto.ApiUserCreateDto;
import me.mppombo.synchronyapi.dto.ApiUserResDto;

/*
 * Represents a registered user of the API. Contains "basic information", username, and password.
 * "Basic information" includes:
 * - email
 * - first name
 * - last name
 */
@Entity
public class ApiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    protected ApiUser() { }

    public ApiUser(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static ApiUser fromCreateDto(ApiUserCreateDto dto) {
        return new ApiUser(dto.username(), dto.password(), dto.email(), dto.firstName(), dto.lastName());
    }

    public ApiUserResDto toResDto() {
        return new ApiUserResDto(id, username, email, firstName, lastName);
    }


    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', email='%s', firstName='%s', lastName='%s']",
                id, username, email, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
