package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.utility.assembler.ApiUserModelAssembler;
import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.domain.ApiUser;
import me.mppombo.synchronyapi.exception.ApiUserNotFoundException;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import me.mppombo.synchronyapi.utility.assembler.ErrorUserExistsModelAssembler;
import me.mppombo.synchronyapi.utility.error.ErrorUserExistsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ApiUserService {
    private final Logger logger = LoggerFactory.getLogger(ApiUserService.class);

    private final ApiUserRepository repository;
    private final ApiUserModelAssembler userModelAssembler;
    private final ErrorUserExistsModelAssembler errorUserExistsModelAssembler;

    public ApiUserService(ApiUserRepository repo, ApiUserModelAssembler userAssembler, ErrorUserExistsModelAssembler userExistsAssembler) {
        this.repository = repo;
        this.userModelAssembler = userAssembler;
        this.errorUserExistsModelAssembler = userExistsAssembler;
    }


    // Return a single user object.
    public EntityModel<ApiUser> getSingleUser(Long id) {
        ApiUser user = repository.findById(id).orElseThrow(() -> new ApiUserNotFoundException(id));
        return userModelAssembler.toModel(user);
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
    public ResponseEntity<?> registerNewUser(ApiUser newUser) {
        List<ApiUser> sameUsernames = repository.findByUsername(newUser.getUsername());
        if (!sameUsernames.isEmpty()) {
            logger.info(String.format("User '%s' exists, sending 409", newUser.getUsername()));
            ApiUser existingUser = sameUsernames.get(0);
            ErrorUserExistsResponse existsRes = new ErrorUserExistsResponse(existingUser.getId(), existingUser.getUsername());
            EntityModel<ErrorUserExistsResponse> userExistsBody = errorUserExistsModelAssembler.toModel(existsRes);
            return ResponseEntity.status(existsRes.getStatus()).body(userExistsBody);
        }

        ApiUser savedUser = repository.save(newUser);
        logger.info(String.format("Registered '%s'", savedUser));
        EntityModel<ApiUser> newUserModel = userModelAssembler.toModel(savedUser);
        return ResponseEntity
                .created(newUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newUserModel);
    }
}
