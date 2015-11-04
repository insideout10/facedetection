package eu.micoproject.facedetection.model.betafaceapi;

import lombok.Data;

/**
 * Represents a BetaFaceApi PointInfo structure.
 *
 * @since 1.0.0
 */
@Data
public class PointInfo {

    private String name;
    private Integer type;
    private Double x;
    private Double y;

}
