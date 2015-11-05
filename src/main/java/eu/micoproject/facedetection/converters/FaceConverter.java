package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.FaceInfo;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;

/**
 * Provides conversion to {@link Face} instances.
 *
 * @since 1.0.0
 */
@Converter
public class FaceConverter implements TypeConverters {

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

}
