package eu.micoproject.facedetection.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The author of a {@link Face} detection.
 *
 * @since 1.0.0
 */
@Data
@Entity
@NoArgsConstructor
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the author.
     *
     * @since 1.0.0
     */
    private String name;

    /**
     * The color to represent the author in the UI.
     *
     * @since 1.0.0
     */
    private String color;

    /**
     * The list of {@link Face}s created by this author.
     *
     * @since 1.0.0
     */
    @OneToMany(mappedBy = "author")
    private List<Face> faces = new ArrayList<>();

}
