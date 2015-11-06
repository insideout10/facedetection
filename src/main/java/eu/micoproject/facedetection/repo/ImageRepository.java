package eu.micoproject.facedetection.repo;

import eu.micoproject.facedetection.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Provides persistence of {@link Image}s.
 *
 * @since 1.0.0
 */
public interface ImageRepository extends CrudRepository<Image, Long> {

}
