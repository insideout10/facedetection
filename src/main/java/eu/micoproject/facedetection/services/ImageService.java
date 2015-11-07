package eu.micoproject.facedetection.services;

import eu.micoproject.facedetection.model.Face;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.ffmpeg.FFmpeg;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.component.direct.DirectConsumer;
import org.apache.camel.component.direct.DirectEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Provides service for generating thumbnails.
 *
 * @since 1.0.0
 */
@Slf4j
@Service
public class ImageService {

    /**
     * The FFmpeg path.
     *
     * @since 1.0.0
     */
    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    /**
     * The height of the thumbnail.
     *
     * @since 1.0.0
     */
    @Value("${facedetection.thumbnail.height:120}")
    private Long scaleHeight;

    /**
     * The Camel endpoint.
     *
     * @since 1.0.0
     */
    @EndpointInject(uri = "direct:inbox")
    private Endpoint endpoint;

    /**
     * The {@link ProducerTemplate} to send an image to Camel.
     *
     * @since 1.0.0
     */
    @Autowired
    private ProducerTemplate producer;

    /**
     * The Process service.
     *
     * @since 1.0.0
     */
    @Autowired
    private ProcessService processService;

    /**
     * The {@link ImageRepository}.
     *
     * @since 1.0.0
     */
    @Autowired
    private ImageRepository imageRepository;

    /**
     * Get a thumbnail for the {@link eu.micoproject.facedetection.model.Image} with the specified id.
     *
     * @param id The {@link eu.micoproject.facedetection.model.Image} id.
     * @return A {@link FileSystemResource} on the generated thumbnail.
     * @throws IOException
     * @throws InterruptedException
     * @since 1.0.0
     */
    public FileSystemResource getThumbnail(final Long id) throws IOException, InterruptedException {

        // Get the input, the output and ensure the output containing folder exists.
        final String input = imageRepository.findOne(id).getAbsoluteFilePath();
        final String output = File.createTempFile("facedetection-", ".png").getAbsolutePath();
        new File(output).getParentFile().mkdirs();

        // Prepare the FFMPEG command line.
        final FFmpeg ffmpeg = new FFmpeg.Builder()
                .ffmpeg(ffmpegPath)
                .overwriteOutput()
                .input(input)
                .scale(-1L, scaleHeight)
                .output(output)
                .build();

        // Run the FFMPEG command and wait max 5 minutes.
        processService.execute(ffmpeg.getCommandLine(), 300L);

        // Return the File System resource on the output.
        return new FileSystemResource(output);
    }

    /**
     * Get the source image for the {@link eu.micoproject.facedetection.model.Image} with the specified id.
     *
     * @param id The {@link eu.micoproject.facedetection.model.Image} id.
     * @return A {@link FileSystemResource} on the source file.
     * @throws IOException
     * @throws InterruptedException
     * @since 1.0.0
     */
    public FileSystemResource getSource(final Long id) {

        // Return the File System resource on the output.
        return new FileSystemResource(imageRepository.findOne(id).getAbsoluteFilePath());
    }

    /**
     * Submit an {@link Image} for processing.
     *
     * @param id The {@link Image} id.
     * @since 1.0.0
     */
    public void submit(final Long id) {

        submit(imageRepository.findOne(id));

    }

    /**
     * Submit an {@link Image} for processing.
     *
     * @param image The {@link Image}.
     * @since 1.0.0
     */
    public void submit(final Image image) {

        log.info("An analysis request started.");

        // Process the route synchronously (to avoid overloading the external service).
        final Object response = producer.requestBody(endpoint, image);

        log.info(String.format("An analysis request completed [ response :: %s ]", response.toString()));

//        final Long authorId = exchange.getIn().getHeader("FaceDetectionAuthorId", Long.class);
//        final Face[] faces = (Face[]) exchange.getIn().getBody();
//
//        log.info(String.format("Found %d faces [ author id :: %d ][ image :: %s ]", faces.length, authorId, image.getFilename()));

    }

    /**
     * Submit all {@link Image}s for processing.
     *
     * @since 1.0.0
     */
    public void submit() {

        imageRepository.findAll().forEach(this::submit);

    }

}
