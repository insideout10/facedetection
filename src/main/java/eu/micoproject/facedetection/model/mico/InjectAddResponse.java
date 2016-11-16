package eu.micoproject.facedetection.model.mico;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InjectAddResponse extends UriResponse {

    private String partUri;

}
