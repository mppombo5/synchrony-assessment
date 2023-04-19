package me.mppombo.synchronyapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/*
 * Represents a registered user of the API. Contains "basic information", username, and password.
 * "Basic information" includes (for now):
 * - first name
 * - last name
 *
 * For now, username and password will be immutable (i.e. no setter functions). This might be changed in the future if
 * endpoints are created for changing account info.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;

    protected User() { }

    public User(String username, String passwordHash, String firstName, String lastName) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username='%s', firstName='%s', lastName='%s']",
                id, username, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
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
