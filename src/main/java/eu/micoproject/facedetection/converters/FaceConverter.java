package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.FaceInfo;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides conversion to {@link Face} instances.
 *
 * @since 1.0.0
 */
@Converter
public class FaceConverter implements TypeConverters {

    /**
     * @since 1.0.0
     */
    private final Pattern XWYH_PATTERN = Pattern.compile("#xywh=(\\d+),(\\d+),(\\d+),(\\d+)");

    /**
     * Converts a {@link FaceInfo} to a {@link Face}.
     *
     * @param source The {@link FaceInfo}.
     * @return A {@link Face} instance.
     * @since 1.0.0
     */
    @Converter
    public Face toFace(FaceInfo source) {

        final Double x = source.getCenterX() - (source.getWidth() / 2);
        final Double y = source.getCenterY() - (source.getHeight() / 2);
        return new Face(x, y, source.getWidth(), source.getHeight());
    }

    /**
     * Converts a {@link BetafaceImageInfoResponse} to an array of {@link Face}s.
     *
     * @param source A {@link BetafaceImageInfoResponse} instance.
     * @return An array of {@link Face}s.
     */
    @Converter
    public Face[] toFaceArray(BetafaceImageInfoResponse source) {

        // Use type converter to convert the single FaceInfo instances, then
        // collect them to a list.
        return source.getFaces().stream()
                .map(this::toFace)
                .toArray(Face[]::new);
    }

    /**
     * Converts an {@link InputStream} to an array of {@link Face}s.
     *
     * @param source An {@link InputStream} instance.
     * @return An array of {@link Face}s.
     */
    @Converter
    public Face[] toFaceArray(InputStream source) throws IOException {

        return IOUtils.readLines(source).stream()
                .map(XWYH_PATTERN::matcher)
                .filter(Matcher::find)
                .map(m -> new Face(Double.valueOf(m.group(1)), Double.valueOf(m.group(2)), Double.valueOf(m.group(3)), Double.valueOf(m.group(4))))
                .toArray(Face[]::new);
    }

}
