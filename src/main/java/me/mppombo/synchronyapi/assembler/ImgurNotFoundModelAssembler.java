package me.mppombo.synchronyapi.assembler;

import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.exception.ImgurErrorBody;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImgurNotFoundModelAssembler
        implements RepresentationModelAssembler<ImgurErrorBody, EntityModel<ImgurErrorBody>> {

    // Send them back to /imgur/image to try again
    @Override
    public EntityModel<ImgurErrorBody> toModel(ImgurErrorBody body) {
        return EntityModel.of(body, linkTo(methodOn(ImgurController.class).getImage("")).withRel("imgurGet"));
    }
}