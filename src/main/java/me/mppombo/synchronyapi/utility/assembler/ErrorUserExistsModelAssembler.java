package me.mppombo.synchronyapi.utility.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.utility.error.ErrorUserExistsResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ErrorUserExistsModelAssembler
        implements RepresentationModelAssembler<ErrorUserExistsResponse, EntityModel<ErrorUserExistsResponse>> {
    /*
     * Returns a resource with a link to the user that already has that username as well as the link to try registering
     * again.
     */
    @Override
    public EntityModel<ErrorUserExistsResponse> toModel(ErrorUserExistsResponse res) {
        return EntityModel.of(res,
                linkTo(methodOn(ApiUserController.class).getOneUser(res.getId())).withRel("existingUser"),
                linkTo(methodOn(ApiUserController.class).createUser(null)).withSelfRel());

    }
}
