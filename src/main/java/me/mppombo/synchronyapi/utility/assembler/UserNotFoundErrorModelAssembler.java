package me.mppombo.synchronyapi.utility.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.utility.error.UserNotFoundErrorBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserNotFoundErrorModelAssembler
        implements RepresentationModelAssembler<UserNotFoundErrorBody, EntityModel<UserNotFoundErrorBody>> {
    /*
     * Since the specified user couldn't be found, throw them a link to get all users which *do* exist.
     */
    @Override
    public EntityModel<UserNotFoundErrorBody> toModel(UserNotFoundErrorBody body) {
        return EntityModel.of(body,
                linkTo(methodOn(ApiUserController.class).getAllUsers()).withRel("users"));
    }
}
