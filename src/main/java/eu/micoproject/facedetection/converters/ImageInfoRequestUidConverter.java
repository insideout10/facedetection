package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageResponse;
import eu.micoproject.facedetection.model.betafaceapi.ImageInfoRequestUid;
import org.apache.camel.Converter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Provides conversions to {@link ImageInfoRequestUidConverter}.
 *
 * @since 1.0.0
 */
@Converter
public class ImageInfoRequestUidConverter {

    /**
     * The BetaFaceApi API key.
     *
     * @since 1.0.0
     */
    @Value("${betafaceapi.api.key}")
    private String apiKey;

    /**
     * The BetaFaceApi API secret.
     *
     * @since 1.0.0
     */
    @Value("${betafaceapi.api.secret}")
    private String apiSecret;

    /**
     * Convert the source {@link BetafaceImageInfoResponse} to an {@link ImageInfoRequestUid} by copying the source imageId
     * to the destination imageId property.
     *
     * @param source The source {@link BetafaceImageInfoResponse}.
     * @return The {@link ImageInfoRequestUid} instance.
     * @since 1.0.0
     */
    @Converter
    public ImageInfoRequestUid toImageInfoRequestUid(BetafaceImageInfoResponse source) {

        return new ImageInfoRequestUid(apiKey, apiSecret, source.getImageId());
    }

    /**
     * Convert the source {@link BetafaceImageResponse} to an {@link ImageInfoRequestUid} by copying the source imageId
     * to the destination imageId property.
     *
     * @param source The source {@link BetafaceImageResponse}.
     * @return The {@link ImageInfoRequestUid} instance.
     * @since 1.0.0
     */
    @Converter
    public ImageInfoRequestUid toImageInfoRequestUid(BetafaceImageResponse source) {

        return new ImageInfoRequestUid(apiKey, apiSecret, source.getImageId());
    }


}
