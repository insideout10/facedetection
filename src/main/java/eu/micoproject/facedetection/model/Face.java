package eu.micoproject.facedetection.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Face instance suitable for persistence.
 *
 * @since 1.0.0
 */
@Data
@Entity
@NoArgsConstructor
public class Face implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double width;

    @Column(nullable = false)
    private Double height;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Version
    private Long version;

    @CreatedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date")
    private DateTime createdDate;

    @LastModifiedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate;

    /**
     * Create a Face instance.
     *
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param width  The width.
     * @param height The height.
     * @since 1.0.0
     */
    public Face(Double x, Double y, Double width, Double height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

}
