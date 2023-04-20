package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.utility.assembler.ApiUserModelAssembler;
import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.domain.ApiUser;
import me.mppombo.synchronyapi.exception.ApiUserNotFoundException;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ApiUserService {
    private final ApiUserRepository repository;
    private final ApiUserModelAssembler assembler;

    public ApiUserService(ApiUserRepository repo, ApiUserModelAssembler assembler) {
        this.repository = repo;
        this.assembler = assembler;
    }


    // Return a single user object.
    public EntityModel<ApiUser> getSingleUser(Long id) {
        ApiUser user = repository.findById(id).orElseThrow(() -> new ApiUserNotFoundException(id));
        return assembler.toModel(user);
    }

    // Returns a list of all currently registered users.
    public CollectionModel<EntityModel<ApiUser>> getAllRegisteredUsers() {
        List<EntityModel<ApiUser>> users = repository.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel
                .of(users, linkTo(methodOn(ApiUserController.class).getAllUsers()).withSelfRel());
    }

    // Create a new user and save it to the repository.
    // TODO: check for existing username, create new exception for if it does
    public ResponseEntity<?> registerNewUser(ApiUser newUser) {
        EntityModel<ApiUser> newUserModel = assembler.toModel(repository.save(newUser));
        return ResponseEntity
                .created(newUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(newUserModel);
    }
}
