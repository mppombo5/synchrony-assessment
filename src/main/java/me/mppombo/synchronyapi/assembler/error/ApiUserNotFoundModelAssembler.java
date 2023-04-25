package me.mppombo.synchronyapi.assembler.error;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.dto.error.ApiUserErrorDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserNotFoundModelAssembler
        implements RepresentationModelAssembler<ApiUserErrorDto, EntityModel<ApiUserErrorDto>> {

    // Since the specified user couldn't be found, throw them a link to get all users which *do* exist.
    @Override
    public EntityModel<ApiUserErrorDto> toModel(ApiUserErrorDto body) {
        return EntityModel.of(body, linkTo(methodOn(ApiUserController.class).getAllUsers()).withRel("users"));
    }
}
