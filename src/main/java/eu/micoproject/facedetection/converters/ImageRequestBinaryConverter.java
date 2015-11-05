package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.ImageRequestBinary;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverter;
import org.apache.camel.component.file.GenericFile;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Provides conversions to {@link ImageRequestBinary}.
 *
 * @since 1.0.0
 */
@Converter
public class ImageRequestBinaryConverter {

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
     * The BetaFaceApi detection flags (http://betafaceapi.com/wpa/index.php/documentation).
     *
     * @since 1.0.0
     */
    @Value("${betafaceapi.api.detection.flags:}")
    private String detectionFlags;

    /**
     * The Camel {@link TypeConverter}.
     *
     * @since 1.0.0
     */
    @Autowired
    private TypeConverter typeConverter;

    /**
     * Converts a {@link GenericFile} to an {@link ImageRequestBinary}.
     *
     * @param source The source {@link GenericFile}.
     * @return An {@link ImageRequestBinary} instance.
     * @since 1.0.0
     */
    @Converter
    public ImageRequestBinary toImageRequestBinary(Image source) {

        return this.toImageRequestBinary(source.getFile());
    }

    /**
     * Converts a {@link GenericFile} to an {@link ImageRequestBinary}.
     *
     * @param source The source {@link GenericFile}.
     * @return An {@link ImageRequestBinary} instance.
     * @since 1.0.0
     */
    @Converter
    public ImageRequestBinary toImageRequestBinary(GenericFile source) {

        final byte[] bytes = typeConverter.convertTo(byte[].class, source);
        final String base64 = Base64.encodeBase64String(bytes);

        return new ImageRequestBinary(this.apiKey, this.apiSecret, this.detectionFlags, base64, source.getFileName());
    }

}
