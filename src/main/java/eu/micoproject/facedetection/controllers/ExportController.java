package eu.micoproject.facedetection.controllers;

import eu.micoproject.facedetection.model.Author;
import eu.micoproject.facedetection.model.ExportLine;
import eu.micoproject.facedetection.repo.FaceRepository;
import eu.micoproject.facedetection.repo.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exports the results.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final static Long MANUAL_AUTHOR_ID = 3L;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FaceRepository faceRepository;

    /**
     * Export the matches as a tab-separated value.
     *
     * @param authorId The {@link Author}'s id to check.
     * @param out      The response output.
     * @since 1.0.0
     */
    @RequestMapping(method = RequestMethod.GET, params = {"author"})
    public void export(@RequestParam("author") final Long authorId, final OutputStream out) {

        final List<ExportLine> lines = new ArrayList<>();

        imageRepository.findAll().forEach(image -> {

            // Get the matching rectangles: intersects, contains or is contained
            final List<Rectangle> markers = faceRepository.findAllByAuthorIdAndImageId(authorId, image.getId()).stream()
                    .map(f -> new Rectangle(f.getX().intValue(), f.getY().intValue(), f.getWidth().intValue(), f.getHeight().intValue()))
                    .collect(Collectors.toList());

            faceRepository.findAllByAuthorIdAndImageId(MANUAL_AUTHOR_ID, image.getId()).stream().forEach(referenceFace -> {

                // Get the reference rectangle.
                final Rectangle reference = new Rectangle(referenceFace.getX().intValue(), referenceFace.getY().intValue(), referenceFace.getWidth().intValue(), referenceFace.getHeight().intValue());

                final List<Rectangle> matches = markers.stream()
                        .filter(rect -> rect.intersects(reference) || rect.contains(reference) || reference.contains(rect))
                        .collect(Collectors.toList());

                if (0 < matches.size()) {
                    final String notes = (matches.get(0).intersects(reference) ? "intersects" : (
                            matches.get(0).contains(reference) ? "contains" : "is contained"))
                            + " (" + matches.size() + ")";
                    lines.add(new ExportLine(image.getFilename(), true, false, false, notes));
                    // Remove the true positives.
                    markers.removeAll(matches);
                } else {
                    lines.add(new ExportLine(image.getFilename(), false, false, true, ""));
                }

            });

            // We're left with the false positives.
            markers.forEach(m -> lines.add(new ExportLine(image.getFilename(), false, true, false, "")));

        });

        try (final OutputStreamWriter writer = new OutputStreamWriter(out)) {
            for (final ExportLine line : lines)
                writer.write(String.format("%s\t%s\t%s\t%s\t%s", line.getPath(),
                        line.getTruePositive() ? "1" : "0",
                        line.getFalsePositive() ? "1" : "0",
                        line.getFalseNegative() ? "1" : "0",
                        line.getNotes()) + "\n");
        } catch (IOException e) {

        }


    }

}
