package eu.micoproject.facedetection.model.betafaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Represents a BetafaceImageInfoResponse (http://betafaceapi.com/wpa/index.php/documentation).
 *
 * @since 1.0.0
 */
@Data
@ToString(callSuper=true)
public class BetafaceImageInfoResponse extends BetafaceImageResponse {

    private static final long serialVersionUID = 1L;

    private String checksum;
    @JsonProperty("original_filename")
    private String originalFilename;
    @JsonProperty("uid")
    private String imageId;

    private List<FaceInfo> faces;

}
