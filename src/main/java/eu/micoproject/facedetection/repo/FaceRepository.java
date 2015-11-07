package eu.micoproject.facedetection.repo;

import eu.micoproject.facedetection.model.Author;
import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.projections.FaceInlineAuthor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Persist {@link Face}s.
 *
 * @since 1.0.0
 */
@RepositoryRestResource(excerptProjection = FaceInlineAuthor.class)
public interface FaceRepository extends CrudRepository<Face, Long> {

    /**
     * Find all the {@link Face}s by {@link Author}'s id.
     *
     * @param authorId The {@link Author}'s id.
     * @since 1.0.0
     */
    @Query("from Face f where f.author.id = :authorId")
    List<Face> findAllByAuthorId(@Param("authorId") Long authorId);

    /**
     * Find all the {@link Face}s by {@link Author}'s and {@link Image}'s ids.
     *
     * @param authorId The {@link Author}'s id.
     * @param imageId  The {@link Image}'s id.
     * @since 1.0.0
     */
    @Query("FROM Face f WHERE f.author.id = :authorId AND f.image.id = :imageId")
    List<Face> findAllByAuthorIdAndImageId(@Param("authorId") Long authorId, @Param("imageId") Long imageId);

}
