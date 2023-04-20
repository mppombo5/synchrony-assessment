package me.mppombo.synchronyapi.utility.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.utility.error.ErrorUserExistsBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ErrorUserExistsModelAssembler
        implements RepresentationModelAssembler<ErrorUserExistsBody, EntityModel<ErrorUserExistsBody>> {
    /*
     * Returns a resource with a link to the user that already has that username as well as the link to try registering
     * again.
     */
    @Override
    public EntityModel<ErrorUserExistsBody> toModel(ErrorUserExistsBody body) {
        return EntityModel.of(body,
                linkTo(methodOn(ApiUserController.class).getOneUser(body.getId())).withRel("existingUser"),
                // passed null because it doesn't actually do anything, just gets the link to /register
                linkTo(methodOn(ApiUserController.class).createUser(null)).withSelfRel());

    }
}
