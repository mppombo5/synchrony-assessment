package me.mppombo.synchronyapi.assembler.imgur.ok;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.imgur.PostOkBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
 * I'm not a fan of how this just duplicates what the GetModelAssembler does, but there doesn't seem to be an easy way
 * to merge the two. Perhaps TODO: create a base class that all successful bodies build off of
 */
@Component
public class ImgurPostModelAssembler implements RepresentationModelAssembler<PostOkBody, EntityModel<PostOkBody>> {
    @Override
    public EntityModel<PostOkBody> toModel(PostOkBody body) {
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
