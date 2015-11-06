package eu.micoproject.facedetection.model.ffmpeg;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 4.4.1
 */
@Getter
public class FFmpeg {

    private final String commandLine;

    protected FFmpeg(Builder builder) {

        commandLine =
                (null != builder.getFFmpeg() ? builder.getFFmpeg() + " " : "")
                        + String.join(" ", builder.getArgs());

    }

    public static class Builder {

        private String ffmpeg = null;

        private final List<String> args = new ArrayList<>();

        public Builder ffmpeg(String ffmpeg) {

            this.ffmpeg = ffmpeg;

            return this;
        }

        public Builder input(String input) {

            args.add(String.format("-i %s", input.replaceAll(" ", "\\ ")));

            return this;

        }

        public Builder filter(FFmpegFilter filter) {

            args.add(filter.getArg());

            return this;

        }

        public Builder format(FFmpegFormat format) {

            args.add(format.getArg());

            return this;

        }

        public Builder output(String output) {

            args.add(output.replaceAll(" ", "\\ "));

            return this;

        }

        public Builder overwriteOutput() {

            args.add("-y");

            return this;

        }

        public Builder position(String timecode) {

            args.add("-ss " + timecode);

            return this;

        }

        public Builder videoFrames(Integer frames) {

            args.add("-vframes " + frames);

            return this;

        }

        public Builder metadata(String key, String value) {

            return metadata(key, value, "");
        }

        public Builder metadata(String key, Integer value, String specifier) {

            return metadata(key, value.toString(), specifier);
        }

        public Builder metadata(String key, String value, String specifier) {

            args.add(String.format("-metadata%s %s=%s", (!specifier.isEmpty() ? ":" + specifier : ""), key, value));

            return this;

        }

        public Builder scale(Long width, Long height) {

            args.add(String.format("-vf scale=%d:%d", width, height));

            return this;

        }

        public Builder bitrate(Double bitrate, String specifier) {

            args.add(String.format("-b%s %f", (!specifier.isEmpty() ? ":" + specifier : ""), bitrate));

            return this;

        }

        public Builder videoCodec(String codec) {

            args.add("-vcodec " + codec);

            return this;

        }

        public Builder audioCodec(String codec) {

            args.add("-acodec " + codec);

            return this;

        }

        public Builder profile(String profile, String specifier) {

            args.add(String.format("-profile%s %s", (!specifier.isEmpty() ? ":" + specifier : ""), profile));

            return this;

        }

        public Builder level(String level) {

            args.add("-level " + level);

            return this;
        }

        public FFmpeg build() {

            return new FFmpeg(this);

        }

        public String getFFmpeg() {
            return ffmpeg;
        }

        public List<String> getArgs() {
            return args;
        }

    }

}
