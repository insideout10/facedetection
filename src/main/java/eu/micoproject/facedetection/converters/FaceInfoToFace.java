package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.betafaceapi.FaceInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts BetaFaceApi {@link FaceInfo}s to {@link Face}s.
 *
 * @since 1.0.0
 */
@Component
public class FaceInfoToFace implements Converter<FaceInfo, Face> {

    /**
     * Converts a {@link FaceInfo} to a {@link Face} using the x, y, width and height properties.
     *
     * @param source The {@link FaceInfo} instance.
     * @return A {@link FaceInfo} instance.
     * @since 1.0.0
     */
    @Override
    public Face convert(FaceInfo source) {

        return new Face(source.getX(), source.getY(), source.getWidth(), source.getHeight());
    }

}
