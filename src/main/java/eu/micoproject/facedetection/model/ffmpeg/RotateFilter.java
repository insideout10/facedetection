package eu.micoproject.facedetection.model.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A custom rotate filter that returns the appropriate FFmpeg filter according to the rotate attribute.
 *
 * @since 4.5.0
 */
@Data
@AllArgsConstructor
public class RotateFilter implements FFmpegFilter {

    private Integer rotate;

    public String getArg() {

        // Set the Transpose/HorizontalFlip filter according to the rotate attribute.
        switch (rotate) {
            case 90:
                return new TransposeFilter(1).getArg();
            case 180:
                return new HorizontalFlipFilter().getArg();
            case 270:
                return new TransposeFilter(2).getArg();
        }

        return "";

    }

}
