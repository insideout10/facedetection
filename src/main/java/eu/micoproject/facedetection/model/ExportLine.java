package eu.micoproject.facedetection.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an export line.
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class ExportLine {

    private String path;

    private Boolean truePositive;

    private Boolean falsePositive;

    private Boolean falseNegative;

    private String notes;

}
