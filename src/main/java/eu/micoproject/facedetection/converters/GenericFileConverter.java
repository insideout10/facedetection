package eu.micoproject.facedetection.converters;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.ImageRequestBinary;
import org.apache.camel.Converter;
import org.apache.camel.component.file.GenericFile;

/**
 * Provides conversions to {@link GenericFile}.
 *
 * @since 1.0.0
 */
@Converter
public class GenericFileConverter {

//    /**
//     * Converts an {@link Image} to a {@link GenericFile}.
//     *
//     * @param source The source {@link Image}.
//     * @return An {@link ImageRequestBinary} instance.
//     * @since 1.0.0
//     */
//    @Converter
//    public GenericFile toGenericFile(Image source) {
//
//        return source.getFile();
//    }

}
