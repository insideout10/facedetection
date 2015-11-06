package eu.micoproject.facedetection.routes;

import eu.micoproject.facedetection.model.Author;
import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.ffmpeg.CropFilter;
import eu.micoproject.facedetection.model.ffmpeg.FFmpeg;
import eu.micoproject.facedetection.repo.AuthorRepository;
import eu.micoproject.facedetection.repo.FaceRepository;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A route to process incoming images.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class InboxRoute extends RouteBuilder {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Value("${facedetection.outbox:/tmp}")
    private String outboxPath;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Override
    public void configure() throws Exception {

//        from("file://{{facedetection.inbox}}")
//                // Convert the Generic File to an Image instance.
//                .convertBodyTo(Image.class)
//                // Persist the Image to the datastore and set it as the new exchange body, the Image Id is stored
//                // in the FaceDetectionImageId header.
//                .process(x -> {
//                    final Image image = imageRepository.save((Image) x.getIn().getBody());
//                    x.getIn().setHeader("FaceDetectionImageId", image.getId());
//                    x.getIn().setBody(image);
//                    x.setOut(x.getIn());
//                })
////                .to("direct:mico.upload_new_image_file");
//                .to("direct:betafaceapi.upload_new_image_file");

        from("direct:inbox")
                .process(x -> {

                    // Set the header image id.
                    final Image image = ((Image) x.getIn().getBody());
                    x.getIn().setHeader("FaceDetectionImageId", image.getId());

                    // Pass over the message.
                    x.setOut(x.getIn());
                })
                .multicast().parallelProcessing()
                .to(
                        "direct:mico.upload_new_image_file",
                        "direct:betafaceapi.upload_new_image_file"
                )
                .end();

        from("direct:persist")
                // Need to handle a response in progress (checksum = null)
                .convertBodyTo(Face[].class)
                .process(x -> {

                    // Get the Image Id from the header, load the instance from the datastore.
                    final Long imageId = (Long) x.getIn().getHeader("FaceDetectionImageId");
                    final Image image = imageRepository.findOne(imageId);

                    final Long authorId = (Long) x.getIn().getHeader("FaceDetectionAuthorId");
                    final Author author = authorRepository.findOne(authorId);

                    // Parse all the faces and add them to the image, then persist everything to the datastore.
                    final Face[] faces = (Face[]) x.getIn().getBody();
                    for (final Face face : faces) {
                        image.getFaces().add(face);
                        face.setImage(image);
                        face.setAuthor(author);
                        faceRepository.save(face);
                    }
                    imageRepository.save(image);

                    // Pass over the message.
                    x.setOut(x.getIn());

                })
//                // Split each face.
//                .split(body())
//                // Process each face, by generating the thumbnails.
//                .process(ex -> {
//                    // Get the the Face and the coordinates.
//                    final Face face = (Face) ex.getIn().getBody();
//                    final Integer x = face.getX().intValue();
//                    final Integer y = face.getY().intValue();
//                    final Integer width = face.getWidth().intValue();
//                    final Integer height = face.getHeight().intValue();
//
//                    // Get the input filename and prepare the output filename.
//                    final String inputPath = face.getImage().getAbsoluteFilePath();
//                    final String filename = face.getImage().getFilename();
//                    final String output = String.format("%s/%s_%d-%d-%d-%d.png", outboxPath, filename, x, y, width, height);
//
//                    // Create the output folders.
//                    final File outputFile = new File(output);
//                    outputFile.getParentFile().mkdirs();
//
//                    // Prepare FFMPEG.
//                    final FFmpeg ffmpeg = new FFmpeg.Builder()
//                            .ffmpeg(ffmpegPath)
//                            .input(inputPath)
//                            .filter(new CropFilter(x, y, width, height))
//                            .output(output)
//                            .build();
//
//                    // Run the process.
//                    final String commandLine = ffmpeg.getCommandLine();
//                    final Process process = Runtime.getRuntime().exec(ffmpeg.getCommandLine());
//                    process.waitFor(60, TimeUnit.MINUTES);
//
//                    final String errors = IOUtils.toString(process.getErrorStream());
//                    final String outputStream = IOUtils.toString(process.getInputStream());
//
//                    log.debug(String.format("Encoding process ended [ command-line :: %s ][ errors :: %s ][ output :: %s ]", commandLine, errors, outputStream));
//
//                    // Forward the message.
//                    ex.setOut(ex.getIn());
//                })
                .to("log:complete")
                .to("mock:foo");

    }

}
