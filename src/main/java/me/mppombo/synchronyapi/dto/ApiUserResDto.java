package me.mppombo.synchronyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.server.core.Relation;

/*
 * The JSON object that we send when responding with user information.
 * Basically has all fields except for the password, and it includes the list of images associated with the account.
 */
@Relation(itemRelation = "user", collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiUserResDto(
        @JsonIgnore     // just used to build links, not a part of the response
        Long id,

        String username,
        String email,
        String firstName,
        String lastName) { }
