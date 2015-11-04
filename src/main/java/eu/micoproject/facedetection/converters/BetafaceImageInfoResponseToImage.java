package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link BetafaceImageInfoResponse} to an {@link Image}.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class BetafaceImageInfoResponseToImage implements Converter<BetafaceImageInfoResponse, Image> {

    @Autowired
    private FaceInfoToFace faceInfoToFace;

    @Override
    public Image convert(BetafaceImageInfoResponse source) {

        // Create the image instance.
        final Image image = new Image(source.getOriginalFilename());

        // Convert the FaceInfo instances in the source and bind them to the Image.
        if (null != source.getFaces()) {
            source.getFaces().stream()
                    .map(faceInfoToFace::convert)
                    .forEach(f -> {
                        image.getFaces().add(f);
                        f.setImage(image);
                    });
        }

        return image;
    }

}
