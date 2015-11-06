package eu.micoproject.facedetection.routes;

import com.sun.tools.javac.jvm.Gen;
import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.mico.UriResponse;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the route to publish to MICO.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class MicoRoute extends RouteBuilder {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void configure() throws Exception {

        final Map<String, Object> images = new HashMap<>();

        final CsvDataFormat csv = new CsvDataFormat();
        csv.setSkipHeaderRecord(true);

        from("direct:mico.upload_new_image_file")
                // The author id is associated with the faces discovered by this service.
                .setHeader("FaceDetectionAuthorId", constant(2L))
                .process(x -> {
                    // Remove the body.
                    x.getIn().setBody(null);
                    x.setOut(x.getIn());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("http4:{{mico.api.server}}/broker/inject/create?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .unmarshal().json(JsonLibrary.Jackson, UriResponse.class)
                .process(x -> {

                    // Store the MICO Item URI.
                    x.getIn().setHeader("MicoItemUri", ((UriResponse) x.getIn().getBody()).getUri());

                    // Get the image id, load the image file and send it to MICO.
                    final Long imageId = x.getIn().getHeader("FaceDetectionImageId", Long.class);
                    final Image image = imageRepository.findOne(imageId);
                    final String path = image.getAbsoluteFilePath();
                    final File imageFile = new File(path);
                    x.getIn().setBody(imageFile);
                    x.getIn().setHeader(Exchange.CONTENT_TYPE, URLConnection.guessContentTypeFromName(path));
                    x.getIn().setHeader(Exchange.FILE_NAME, image.getFilename());

                    // log.info(String.format("[ path :: %s ][ content type :: %s ][ filename :: %s ]", image.getAbsoluteFilePath(), x.getIn().getHeader(Exchange.CONTENT_TYPE), x.getIn().getHeader(Exchange.FILE_NAME)));

                    // Passover the message.
                    x.setOut(x.getIn());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_QUERY, simple("ci=${header.MicoItemUri}&type=${header.Content-Type}&name=${header.CamelFileName}"))
                .to("http4:{{mico.api.server}}/broker/inject/add?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .unmarshal().json(JsonLibrary.Jackson, UriResponse.class)
                .process(x -> {

                    // Record the part URI.
                    x.getIn().setHeader("MicoPartUri", ((UriResponse) x.getIn().getBody()).getUri());
                    x.getIn().setBody(null);

                    // Pass over the message.
                    x.setOut(x.getIn());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_QUERY, simple("ci=${header.MicoItemUri}"))
                .to("http4:{{mico.api.server}}/broker/inject/submit?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .process(x -> {

                    Thread.sleep(3000);
                    x.getIn().setHeader(Exchange.HTTP_QUERY, null);

                    // Pass over the message.
                    x.setOut(x.getIn());

                })
                .setHeader("Accept", constant("text/csv"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/sparql-query"))
                .setHeader(Exchange.CONTENT_ENCODING, constant("UTF-8"))
                .setBody(simple("PREFIX mico: <http://www.mico-project.eu/ns/platform/1.0/schema#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX oa: <http://www.w3.org/ns/oa#>\n" +
                        "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
                        "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                        "\n" +
                        "SELECT DISTINCT ?region WHERE {\n" +
                        " <${header.MicoItemUri}> mico:hasContentPart ?cp .\n" +
                        "   ?cp mico:hasContent ?annot .\n" +
                        "   ?annot oa:hasBody ?body .\n" +
                        "   ?annot oa:hasTarget ?tgt .\n" +
                        "   ?tgt  oa:hasSelector ?fs .\n" +
                        "   ?fs rdf:value ?region\n" +
                        " FILTER EXISTS {?body rdf:type mico:FaceDetectionBody}\n" +
                        "}\n"))
                .to("http4:{{mico.api.server}}/marmotta/sparql/select?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .to("direct:persist");

    }

}
