package eu.micoproject.facedetection.repo;

import eu.micoproject.facedetection.model.Face;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Persist {@link Face}s.
 *
 * @since 1.0.0
 */
public interface FaceRepository extends PagingAndSortingRepository<Face, Long> {
}
