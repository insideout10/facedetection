package eu.micoproject.facedetection.repo;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.projections.FaceInlineAuthor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Persist {@link Face}s.
 *
 * @since 1.0.0
 */
@RepositoryRestResource(excerptProjection = FaceInlineAuthor.class)
public interface FaceRepository extends CrudRepository<Face, Long> {
}
