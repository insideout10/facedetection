package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts an {@link InputStream} to an {@link Image}.
 *
 * @since 1.0.0
 */
@Component
public class InputStreamToImage implements Converter<InputStream, Image> {

    private final static Pattern XYWH_PATTERN = Pattern.compile("#xywh=(\\d+),(\\d+),(\\d+),(\\d+)");

    @Override
    public Image convert(InputStream source) {

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

}
