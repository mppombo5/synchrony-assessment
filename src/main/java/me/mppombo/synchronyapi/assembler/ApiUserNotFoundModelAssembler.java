package me.mppombo.synchronyapi.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.exception.ApiUserErrorBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserNotFoundModelAssembler
        implements RepresentationModelAssembler<ApiUserErrorBody, EntityModel<ApiUserErrorBody>> {

    // Since the specified user couldn't be found, throw them a link to get all users which *do* exist.
    @Override
    public EntityModel<ApiUserErrorBody> toModel(ApiUserErrorBody body) {
        return EntityModel.of(body, linkTo(methodOn(ApiUserController.class).getAllUsers()).withRel("users"));
    }
}
