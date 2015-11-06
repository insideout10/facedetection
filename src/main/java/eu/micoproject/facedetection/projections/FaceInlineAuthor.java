package eu.micoproject.facedetection.projections;

import eu.micoproject.facedetection.model.Author;
import eu.micoproject.facedetection.model.Face;
import org.springframework.data.rest.core.config.Projection;

/**
 * Defines a projection to explicitly output the {@link Author} property in REST lists.
 *
 * @since 1.0.0
 */
@Projection(name = "inlineAuthor", types = {Face.class})
public interface FaceInlineAuthor {

    Double getX();
    Double getY();
    Double getWidth();
    Double getHeight();

    Author getAuthor();

}
