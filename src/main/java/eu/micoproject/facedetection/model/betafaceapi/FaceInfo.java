package eu.micoproject.facedetection.model.betafaceapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Represents a BetaFaceApi FaceInfo structure (http://betafaceapi.com/wpa/index.php/documentation).
 *
 * @since 1.0.0
 */
@Data
public class FaceInfo {

    private Double angle;
    private Double height;
    private Double score;
    private String uid;
    private Double width;
    private Double x;
    private Double y;
    @JsonProperty("image_uid")
    private String imageId;
    @JsonProperty("person_name")
    private String personName;

    private List<PointInfo> points;
    private List<TagInfo> tags;
    @JsonProperty("user_points")
    private List<PointInfo> userPoints;

}
