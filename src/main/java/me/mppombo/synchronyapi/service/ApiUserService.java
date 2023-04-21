package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.utility.assembler.ApiUserModelAssembler;
import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.models.ApiUser;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import me.mppombo.synchronyapi.utility.assembler.ErrorUserExistsModelAssembler;
import me.mppombo.synchronyapi.utility.assembler.ErrorUserNotFoundModelAssembler;
import me.mppombo.synchronyapi.utility.error.ErrorUserExistsBody;
import me.mppombo.synchronyapi.utility.error.ErrorUserNotFoundBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ApiUserService {
    private final Logger logger = LoggerFactory.getLogger(ApiUserService.class);

    private final ApiUserRepository repository;
    private final ApiUserModelAssembler userModelAssembler;
    private final ErrorUserExistsModelAssembler errorUserExistsModelAssembler;
    private final ErrorUserNotFoundModelAssembler errorUserNotFoundModelAssembler;

    public ApiUserService(
            ApiUserRepository repo,
            ApiUserModelAssembler userAssembler,
            ErrorUserExistsModelAssembler userExistsAssembler,
            ErrorUserNotFoundModelAssembler userNotFoundAssembler) {
        this.repository = repo;
        this.userModelAssembler = userAssembler;
        this.errorUserExistsModelAssembler = userExistsAssembler;
        this.errorUserNotFoundModelAssembler = userNotFoundAssembler;
    }


    /*
     * Queries and returns a single user object by ID.
     * Returns a 404 if a user with the specified ID does not exist.
     */
    public ResponseEntity<EntityModel<?>> getSingleUser(Long id) {
        Optional<ApiUser> user = repository.findById(id);
        if (user.isEmpty()) {
            logger.info(String.format("Requested user with ID=%d does not exist, sending 404", id));
            ErrorUserNotFoundBody notFoundBody = new ErrorUserNotFoundBody(id);
            EntityModel<ErrorUserNotFoundBody> notFoundModel = errorUserNotFoundModelAssembler.toModel(notFoundBody);
            return ResponseEntity.status(notFoundBody.getStatus()).body(notFoundModel);
        }

        logger.info(String.format("Found %s", user.get()));
        EntityModel<ApiUser> foundUserModel = userModelAssembler.toModel(user.get());
        return ResponseEntity.ok(foundUserModel);
    }

    // Returns a list of all currently registered users.
    public CollectionModel<EntityModel<ApiUser>> getAllRegisteredUsers() {
        List<EntityModel<ApiUser>> users = repository.findAll().stream().map(userModelAssembler::toModel).toList();
        return CollectionModel
                .of(users, linkTo(methodOn(ApiUserController.class).getAllUsers()).withSelfRel());
    }

    /*
     * Create a new user and save it to the repository.
     * The single constraint is that a new user cannot be created with a username that is already registered.
     */
    public ResponseEntity<EntityModel<?>> registerNewUser(ApiUser newUser) {
        List<ApiUser> sameUsernames = repository.findByUsername(newUser.getUsername());
        if (!sameUsernames.isEmpty()) {
            logger.info(String.format("User '%s' exists, sending 409", newUser.getUsername()));
            ApiUser existingUser = sameUsernames.get(0);
            ErrorUserExistsBody userExistsBody = new ErrorUserExistsBody(existingUser.getId(), existingUser.getUsername());
            EntityModel<ErrorUserExistsBody> userExistsModel = errorUserExistsModelAssembler.toModel(userExistsBody);
            return ResponseEntity.status(userExistsBody.getStatus()).body(userExistsModel);
        }

        ApiUser savedUser = repository.save(newUser);
        logger.info(String.format("Registered '%s'", savedUser));
        EntityModel<ApiUser> newUserModel = userModelAssembler.toModel(savedUser);
        return ResponseEntity
                .created(newUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newUserModel);
    }
}
