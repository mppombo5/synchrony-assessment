package me.mppombo.synchronyapi.assembler.error;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.error.ImgurErrorDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImgurBadRequestModelAssembler
        implements RepresentationModelAssembler<ImgurErrorDto, EntityModel<ImgurErrorDto>> {

    // Send them back to the POST endpoint to try again
    @Override
    public EntityModel<ImgurErrorDto> toModel(ImgurErrorDto body) {
        return EntityModel.of(
                body,
                linkTo(methodOn(ImgurController.class)
                        .uploadImage(null, null, null, null))
                        .withRel("imgurPost"));
    }
}
