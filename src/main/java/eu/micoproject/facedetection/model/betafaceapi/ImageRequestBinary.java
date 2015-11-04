package eu.micoproject.facedetection.model.betafaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a BetaFaceApi image request binary (http://betafaceapi.com/service_json.svc/help/operations/UploadNewImage_File#request-xml).
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class ImageRequestBinary {

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("api_secret")
    private String apiSecret;

    @JsonProperty("detection_flags")
    private String detectionFlags;

    @JsonProperty("imagefile_data")
    private String imageFileData;

    @JsonProperty("original_filename")
    private String originalFilename;

}
