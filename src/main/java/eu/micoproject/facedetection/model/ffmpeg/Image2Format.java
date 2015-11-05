package eu.micoproject.facedetection.model.ffmpeg;

/**
 * @since 4.4.1
 */
public class Image2Format implements FFmpegFormat {

    public String getArg() {

        return "-f image2";

    }

}
