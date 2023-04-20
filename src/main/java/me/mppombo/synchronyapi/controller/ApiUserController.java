package me.mppombo.synchronyapi.controller;

import me.mppombo.synchronyapi.domain.ApiUser;
import me.mppombo.synchronyapi.service.ApiUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiUserController {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    private final ApiUserService service;

    public ApiUserController(ApiUserService service) {
        this.service = service;
    }


    // Aggregate all registered users
    @GetMapping("/users")
    public CollectionModel<EntityModel<ApiUser>> getAllUsers() {
        logger.info("Processing request for aggregate information of all users");
        return service.getAllRegisteredUsers();
    }

    // Get a single user by ID
    // TODO: add searching function by username/last name
    @GetMapping("/users/{id}")
    public EntityModel<ApiUser> getOneUser(@PathVariable Long id) {
        logger.info(String.format("Processing request for user data with ID %d", id));
        return service.getSingleUser(id);
    }

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody ApiUser newUser) {
        logger.info(String.format("Request to register new user '%s'", newUser.getUsername()));
        return service.registerNewUser(newUser);
    }
}
