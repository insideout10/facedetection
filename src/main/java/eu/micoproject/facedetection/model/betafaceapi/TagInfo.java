package eu.micoproject.facedetection.model.betafaceapi;

import lombok.Data;

/**
 * Represents a BetaFaceApi TagInfo structure (http://betafaceapi.com/wpa/index.php/documentation).
 *
 * @since 1.0.0
 */
@Data
public class TagInfo {

    private Double confidence;
    private String name;
    private String value;

}
