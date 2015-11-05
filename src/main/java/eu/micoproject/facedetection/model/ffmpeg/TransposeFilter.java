package eu.micoproject.facedetection.model.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A transpose video filter for {@link FFmpeg}.
 *
 * @since 4.5.0
 */
@Data
@AllArgsConstructor
public class TransposeFilter implements FFmpegFilter {

    private final static String TEMPLATE = "-vf transpose=%d";

    private Integer transpose;

    @Override
    public String getArg() {

        return String.format(TEMPLATE, transpose);

    }

}
