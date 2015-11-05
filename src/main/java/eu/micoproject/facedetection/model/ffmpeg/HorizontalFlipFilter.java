package eu.micoproject.facedetection.model.ffmpeg;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A horizontal flip video filter for {@link FFmpeg}.
 *
 * @since 4.5.0
 */
@Data
@AllArgsConstructor
public class HorizontalFlipFilter implements FFmpegFilter {

    private final static String TEMPLATE = "-vf hflip";

    @Override
    public String getArg() {

        return TEMPLATE;

    }

}
