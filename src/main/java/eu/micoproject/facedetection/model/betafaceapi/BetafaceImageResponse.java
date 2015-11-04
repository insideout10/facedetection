package eu.micoproject.facedetection.model.betafaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * A response from the BetaFaceApi service.
 *
 * @since 1.0.0
 */
@Data
public class BetafaceImageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("int_response")
    private Integer code;

    @JsonProperty("string_response")
    private String message;

    @JsonProperty("img_uid")
    private String imageId;

}
