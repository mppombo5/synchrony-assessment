package me.mppombo.synchronyapi.assembler.imgur.ok;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.imgur.DeleteOkBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImgurDeleteModelAssembler
        implements RepresentationModelAssembler<DeleteOkBody, EntityModel<DeleteOkBody>> {

    @Override
    public EntityModel<DeleteOkBody> toModel(DeleteOkBody body) {
        return EntityModel.of(
                body,
                linkTo(methodOn(ImgurController.class).getImage("")).withRel("imgurGet"),
                linkTo(methodOn(ImgurController.class)
                        .uploadImage(null, null, null))
                        .withRel("imgurPost"),
                linkTo(methodOn(ImgurController.class).deleteImage("")).withRel("imgurDelete"));
    }
}
