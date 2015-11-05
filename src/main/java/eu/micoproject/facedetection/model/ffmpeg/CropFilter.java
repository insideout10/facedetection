package eu.micoproject.facedetection.model.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A crop filter for {@link FFmpeg}.
 *
 * @since 4.4.1
 */
@Data
@AllArgsConstructor
public class CropFilter implements FFmpegFilter {

    private final static String TEMPLATE = "-vf crop=%d:%d:%d:%d";

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    @Override
    public String getArg() {

        return String.format(TEMPLATE, width, height, x, y);

    }

}
