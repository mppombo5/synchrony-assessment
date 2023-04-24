package me.mppombo.synchronyapi.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.exception.ApiUserErrorBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserUsernameTakenModelAssembler
        implements RepresentationModelAssembler<ApiUserErrorBody, EntityModel<ApiUserErrorBody>> {

    // If the given username is already taken then send them back to the register link to try again.
    @Override
    public EntityModel<ApiUserErrorBody> toModel(ApiUserErrorBody body) {
        return EntityModel.of(body,
                // passed null because it doesn't actually do anything, just gets the link to /register
                linkTo(methodOn(ApiUserController.class).createUser(null)).withRel("register"));
    }
}
