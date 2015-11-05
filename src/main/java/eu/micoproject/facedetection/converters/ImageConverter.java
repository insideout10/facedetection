package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.ImageRequestBinary;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverter;
import org.apache.camel.component.file.GenericFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides conversions to {@link Image}.
 *
 * @since 1.0.0
 */
@Converter
public class ImageConverter {

    /**
     * The {@link Pattern} to convert a xywh string to coordinates.
     *
     * @since 1.0.0
     */
    private final static Pattern XYWH_PATTERN = Pattern.compile("#xywh=(\\d+),(\\d+),(\\d+),(\\d+)");

    /**
     * The Apache Camel type converter.
     *
     * @since 1.0.0
     */
    @Autowired
    private TypeConverter typeConverter;

    /**
     * Converts an {@link InputStream} to an {@link Image}.
     *
     * @since 1.0.0
     */
    @Converter
    public Image toImage(InputStream source) {

        final Image image = new Image("xyz");

        try (final BufferedReader stream = new BufferedReader(new InputStreamReader(source))) {

            String line;
            while (null != (line = stream.readLine())) {
                final Matcher matcher = XYWH_PATTERN.matcher(line);
                if (matcher.find()) {
                    final Double x = Double.valueOf(matcher.group(1));
                    final Double y = Double.valueOf(matcher.group(2));
                    final Double width = Double.valueOf(matcher.group(3));
                    final Double height = Double.valueOf(matcher.group(4));
                    final Face face = new Face(x, y, width, height);
                    image.getFaces().add(face);
                    face.setImage(image);
                }
            }

        } catch (IOException e) {

        }

        return image;
    }

    /**
     * Converts a {@link GenericFile} to an {@link ImageRequestBinary}.
     *
     * @param source The source {@link GenericFile}.
     * @return An {@link ImageRequestBinary} instance.
     * @since 1.0.0
     */
    @Converter
    public Image toImage(GenericFile source) {

        return new Image(source.getFileName(), source.getAbsoluteFilePath(), source);
    }

    /**
     * Converts a {@link BetafaceImageInfoResponse} to an {@link Image}.
     *
     * @param source A {@link BetafaceImageInfoResponse}.
     * @return A {@link Image}.
     * @since 1.0.0
     */
    @Converter
    public Image toImage(BetafaceImageInfoResponse source) {

        // Create the image instance.
        final Image image = new Image(source.getOriginalFilename());

        // Convert the FaceInfo instances in the source and bind them to the Image.
        if (null != source.getFaces()) {
            source.getFaces().stream()
                    .map(f -> typeConverter.convertTo(Face.class, f))
                    .forEach(f -> {
                        image.getFaces().add(f);
                        f.setImage(image);
                    });
        }

        return image;
    }

}
