package eu.micoproject.facedetection.services;

import eu.micoproject.facedetection.model.ffmpeg.CropFilter;
import eu.micoproject.facedetection.model.ffmpeg.FFmpeg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Executes external processes.
 *
 * @since 1.0.0
 */
@Slf4j
@Service
public class ProcessService {

    /**
     * Executes a command.
     *
     * @param command The command to execute.
     * @param timeout The timeout (in seconds) to wait for completion.
     * @throws InterruptedException
     * @throws IOException
     * @since 1.0.0
     */
    public void execute(final String command, final Long timeout) throws InterruptedException, IOException {

        // Run the process.
        final Process process = Runtime.getRuntime().exec(command);
        process.waitFor(timeout, TimeUnit.SECONDS);

        final String errors = IOUtils.toString(process.getErrorStream());
        final String outputStream = IOUtils.toString(process.getInputStream());

        log.debug(String.format("Encoding process ended [ command-line :: %s ][ errors :: %s ][ output :: %s ]", command, errors, outputStream));

    }
}
