package me.mppombo.synchronyapi.utility.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.domain.ApiUser;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserModelAssembler implements RepresentationModelAssembler<ApiUser, EntityModel<ApiUser>> {
    @Override
    public EntityModel<ApiUser> toModel(ApiUser user) {
        return EntityModel.of(user,
                linkTo(methodOn(ApiUserController.class).getOneUser(user.getId())).withSelfRel(),
                linkTo(methodOn(ApiUserController.class).getAllUsers()).withRel("users"));
    }
}
