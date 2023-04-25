package me.mppombo.synchronyapi.assembler;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.ImgurImageDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImageDtoModelAssembler implements RepresentationModelAssembler<ImgurImageDto, EntityModel<ImgurImageDto>> {
    @Override
    public EntityModel<ImgurImageDto> toModel(ImgurImageDto imageDto) {
        return EntityModel.of(
                imageDto,
                linkTo(methodOn(ImgurController.class).getImage(null, imageDto.imgurId())).withSelfRel(),
                linkTo(methodOn(ImgurController.class)
                        .uploadImage(null, null, null))
                        .withRel("imgurUpload"),
                linkTo(methodOn(ImgurController.class).deleteImage("")).withRel("imgurDelete"));
    }
}
