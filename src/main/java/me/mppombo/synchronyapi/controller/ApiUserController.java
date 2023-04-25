package me.mppombo.synchronyapi.controller;

import jakarta.validation.Valid;
import me.mppombo.synchronyapi.dto.RegisterDto;
import me.mppombo.synchronyapi.dto.ApiUserDto;
import me.mppombo.synchronyapi.model.ApiUser;
import me.mppombo.synchronyapi.service.ApiUserService;
import me.mppombo.synchronyapi.assembler.ApiUserDtoModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class ApiUserController {
    private final static Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    private final ApiUserService service;
    private final ApiUserDtoModelAssembler userDtoModelAssembler;

    public ApiUserController(ApiUserService service, ApiUserDtoModelAssembler userDtoModelAssembler) {
        this.service = service;
        this.userDtoModelAssembler = userDtoModelAssembler;
    }


    // Aggregate all registered users
    @GetMapping({"", "/"})
    public ResponseEntity<CollectionModel<EntityModel<ApiUserDto>>> getAllUsers() {
        logger.info("Processing request for aggregate information of all users");

        List<ApiUser> allUsers = service.getAllRegisteredUsers();
        var allUserDtoModels = allUsers
                .stream()
                .map(ApiUser::toDto)
                .map(userDtoModelAssembler::toModel)
                .toList();
        var userDtosCollection = CollectionModel.of(
                allUserDtoModels,
                linkTo(methodOn(ApiUserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(userDtosCollection);
    }

    // Get a single user by ID
    // TODO: add searching function by username/last name
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ApiUserDto>> getOneUser(@PathVariable Long id) {
        logger.info("Processing request for user data with ID {}", id);

        ApiUser user = service.getSingleUser(id);
        var userModel = userDtoModelAssembler.toModel(user.toDto());

        return ResponseEntity.ok(userModel);
    }

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<EntityModel<ApiUserDto>> createUser(
            @Valid
            @RequestBody
            RegisterDto newUserDto) {
        logger.info("Request to register new user w/ username='{}'", newUserDto.username());

        ApiUser savedUser = service.registerNewUser(ApiUser.fromRegisterDto(newUserDto));
        var savedUserModel = userDtoModelAssembler.toModel(savedUser.toDto());

        return ResponseEntity
                .created(savedUserModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(savedUserModel);
    }
}
