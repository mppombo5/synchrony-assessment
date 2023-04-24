package me.mppombo.synchronyapi.assembler.imgur.ok;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.imgur.GetOkBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
 * Model assembler for GETs/POSTs/DELETEs where everything turns out all fine and dandy
 */
@Component
public class ImgurGetModelAssembler implements RepresentationModelAssembler<GetOkBody, EntityModel<GetOkBody>> {
    @Override
    public EntityModel<GetOkBody> toModel(GetOkBody body) {
        return EntityModel.of(
                body,
                linkTo(methodOn(ImgurController.class).getImage(body.data().id())).withSelfRel(),
                linkTo(methodOn(ImgurController.class).getImage("")).withRel("imgurGet"),
                linkTo(methodOn(ImgurController.class)
                        .uploadImage(null, null, null))
                        .withRel("imgurPost"),
                linkTo(methodOn(ImgurController.class).deleteImage("")).withRel("imgurDelete"));
    }
}
