package eu.micoproject.facedetection.model.betafaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a BetaFaceApi ImageInfoRequestUid structure used to request information about a processed image (http://betafaceapi.com/wpa/index.php/documentation).
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class ImageInfoRequestUid {

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("api_secret")
    private String apiSecret;

    @JsonProperty("img_uid")
    private String imageId;

}
