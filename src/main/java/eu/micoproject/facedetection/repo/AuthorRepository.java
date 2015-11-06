package eu.micoproject.facedetection.repo;

import eu.micoproject.facedetection.model.Author;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Provides persistence for {@link Author}s.
 *
 * @since 1.0.0
 */
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
}
