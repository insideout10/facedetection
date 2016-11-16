package eu.micoproject.facedetection.model.mico;

import lombok.Data;

/**
 * A MICO response with an URI.
 *
 * @since 1.0.0
 */
@Data
public class UriResponse {

    private String itemUri;

    private String created;

}
