package eu.micoproject.facedetection.resources;

import eu.micoproject.facedetection.controllers.ImageController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import eu.micoproject.facedetection.model.Image;

import java.io.IOException;

/**
 * Process {@link Image} resources.
 *
 * @since 1.0.0
 */
@Component
public class ImageProcessor implements ResourceProcessor<Resource<Image>> {

    /**
     * Intercept the {@link Resource} before it is being sent to the client and add the link to the thumbnail.
     *
     * @param resource The output {@link Resource}.
     * @return The output {@link Resource} with the link to the thumbnail.
     * @since 1.0.0
     */
    @Override
    public Resource<Image> process(Resource<Image> resource) {

        try {
            resource.add(linkTo(methodOn(ImageController.class).getThumbnail(resource.getContent().getId())).withRel("thumbnail"));
            resource.add(linkTo(methodOn(ImageController.class).getSource(resource.getContent().getId(), null)).withRel("source"));
        } catch (IOException | InterruptedException e) {

        }
        return resource;

    }

}
