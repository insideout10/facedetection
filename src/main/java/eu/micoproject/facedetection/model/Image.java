package eu.micoproject.facedetection.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.file.GenericFile;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An Image instance.
 *
 * @since 1.0.0
 */
@Data
@Entity
@NoArgsConstructor
public class Image implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 1024, nullable = false)
    private String filename;

    @Column(length = 1024, nullable = false)
    private String absoluteFilePath;

    @OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
    private List<Face> faces = new ArrayList<>();

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
     * Create an Image instance with the specified filename.
     *
     * @param filename
     * @since 1.0.0
     */
    public Image(String filename) {

        this.filename = filename;
    }

    /**
     * Create an Image instance with the specified filename and file source.
     *
     * @param filename The filename.
     * @since 1.0.0
     */
    public Image(String filename, String absoluteFilePath) {
        this(filename);

        this.absoluteFilePath = absoluteFilePath;
    }

}
