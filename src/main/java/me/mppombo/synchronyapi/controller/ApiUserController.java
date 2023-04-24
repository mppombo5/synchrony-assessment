package me.mppombo.synchronyapi.controller;

import me.mppombo.synchronyapi.models.ApiUser;
import me.mppombo.synchronyapi.service.ApiUserService;
import me.mppombo.synchronyapi.assembler.ApiUserModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ApiUserController {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    private final ApiUserService service;
    private final ApiUserModelAssembler userModelAssembler;

    public ApiUserController(ApiUserService service, ApiUserModelAssembler userModelAssembler) {
        this.service = service;
        this.userModelAssembler = userModelAssembler;
    }


    // Aggregate all registered users
    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<ApiUser>>> getAllUsers() {
        logger.info("Processing request for aggregate information of all users");
        var usersCollection = CollectionModel.of(
                service.getAllRegisteredUsers().stream().map(userModelAssembler::toModel).toList(),
                linkTo(methodOn(ApiUserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok().body(usersCollection);
    }

    // Get a single user by ID
    // TODO: add searching function by username/last name
    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<ApiUser>> getOneUser(@PathVariable Long id) {
        logger.info("Processing request for user data with ID {}", id);
        var user = userModelAssembler.toModel(service.getSingleUser(id));

        return ResponseEntity.ok(user);
    }

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<EntityModel<ApiUser>> createUser(@RequestBody ApiUser newUser) {
        logger.info("Request to register new user {}", newUser.getUsername());
        var savedUser = userModelAssembler.toModel(service.registerNewUser(newUser));

        return ResponseEntity.created(savedUser.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(savedUser);
    }
}
