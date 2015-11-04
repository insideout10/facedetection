package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageResponse;
import eu.micoproject.facedetection.model.betafaceapi.ImageInfoRequestUid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link BetafaceImageInfoResponse} to an {@link ImageInfoRequestUid}.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class BetafaceImageInfoResponseToImageInfoRequestUid implements Converter<BetafaceImageInfoResponse, ImageInfoRequestUid> {

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
    @Override
    public ImageInfoRequestUid convert(BetafaceImageInfoResponse source) {

        return new ImageInfoRequestUid(apiKey, apiSecret, source.getImageId());
    }

}
