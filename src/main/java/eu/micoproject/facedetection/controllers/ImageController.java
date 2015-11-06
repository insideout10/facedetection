package eu.micoproject.facedetection.controllers;

import eu.micoproject.facedetection.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import eu.micoproject.facedetection.model.Image;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generates a thumbnail for an image.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/api/images/{id}")
public class ImageController {

    /**
     * The Thumbnail service providing the methods supporting this controller.
     *
     * @since 1.0.0
     */
    @Autowired
    private ImageService imageService;

    /**
     * Get a thumbnail for the specified image.
     *
     * @param id The {@link Image} id.
     * @return A thumbnail.
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/thumbnail", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public FileSystemResource getThumbnail(@PathVariable("id") final Long id) throws IOException, InterruptedException {

        return imageService.getThumbnail(id);
    }

    /**
     * Get the source image.
     *
     * @param id The {@link Image} id.
     * @return The source image.
     * @throws IOException
     * @throws InterruptedException * @since 1.0.0
     */
    @RequestMapping(value = "/source", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public FileSystemResource getSource(@PathVariable("id") final Long id, final HttpServletResponse response) throws IOException, InterruptedException {

        final FileSystemResource resource = imageService.getSource(id);
        response.setContentType(Files.probeContentType(Paths.get(resource.getURI())));
        return resource;
    }

    /**
     * Submit an {@link Image} for processing.
     *
     * @since 1.0.0
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public void submit(@PathVariable("id") final Long id) {

        imageService.submit(id);

    }

}
