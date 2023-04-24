package me.mppombo.synchronyapi.assembler;

import me.mppombo.synchronyapi.controller.ApiUserController;
import me.mppombo.synchronyapi.dto.ApiUserResDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserDtoModelAssembler
        implements RepresentationModelAssembler<ApiUserResDto, EntityModel<ApiUserResDto>> {
    @Override
    public EntityModel<ApiUserResDto> toModel(ApiUserResDto userDto) {
        return EntityModel.of(
                userDto,
                linkTo(methodOn(ApiUserController.class).getOneUser(userDto.id())).withSelfRel(),
                linkTo(methodOn(ApiUserController.class).getAllUsers()).withRel("users"));
    }
}
