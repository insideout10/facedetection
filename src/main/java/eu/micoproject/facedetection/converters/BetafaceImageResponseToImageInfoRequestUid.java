package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageResponse;
import eu.micoproject.facedetection.model.betafaceapi.ImageInfoRequestUid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link BetafaceImageResponse} to an {@link ImageInfoRequestUid} providing a convenient bridge to request
 * Image infos about a recently uploaded image.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class BetafaceImageResponseToImageInfoRequestUid implements Converter<BetafaceImageResponse, ImageInfoRequestUid> {

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
     * Convert the source {@link BetafaceImageResponse} to an {@link ImageInfoRequestUid} by copying the source imageId
     * to the destination imageId property.
     *
     * @param source The source {@link BetafaceImageResponse}.
     * @return The {@link ImageInfoRequestUid} instance.
     * @since 1.0.0
     */
    @Override
    public ImageInfoRequestUid convert(BetafaceImageResponse source) {

        return new ImageInfoRequestUid(apiKey, apiSecret, source.getImageId());
    }

}
