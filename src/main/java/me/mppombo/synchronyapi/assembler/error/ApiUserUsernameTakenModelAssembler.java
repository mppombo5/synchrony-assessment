package me.mppombo.synchronyapi.assembler.error;

import me.mppombo.synchronyapi.controller.AuthController;
import me.mppombo.synchronyapi.dto.error.ApiUserErrorDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserUsernameTakenModelAssembler
        implements RepresentationModelAssembler<ApiUserErrorDto, EntityModel<ApiUserErrorDto>> {

    // If the given username is already taken then send them back to the register link to try again.
    @Override
    public EntityModel<ApiUserErrorDto> toModel(ApiUserErrorDto body) {
        return EntityModel.of(body,
                // passed null because it doesn't actually do anything, just gets the link to /register
                linkTo(methodOn(AuthController.class).registerUser(null)).withRel("register"));
    }
}
