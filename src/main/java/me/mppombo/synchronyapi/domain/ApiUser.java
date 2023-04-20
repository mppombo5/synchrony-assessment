package me.mppombo.synchronyapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.hateoas.server.core.Relation;

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
@Relation(itemRelation = "user", collectionRelation = "users")
public class ApiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;

    protected ApiUser() { }

    public ApiUser(String username, String password, String firstName, String lastName) {
        this.username = username;
        // TODO: hash password
        this.password = password;
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

    public String getPassword() {
        return password;
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
