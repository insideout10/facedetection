package eu.micoproject.facedetection.routes;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.mico.InjectAddResponse;
import eu.micoproject.facedetection.model.mico.UriResponse;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URLConnection;

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

        final CsvDataFormat csv = new CsvDataFormat();
        csv.setSkipHeaderRecord(true);

        from("direct:mico.upload_new_image_file")
                .errorHandler(defaultErrorHandler().redeliveryDelay(1000).maximumRedeliveries(10).log("Retrying..."))
                // The author id is associated with the faces discovered by this service.
                .setHeader("FaceDetectionAuthorId", constant(5L))
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
                    x.getIn().setHeader("MicoItemUri", ((UriResponse) x.getIn().getBody()).getItemUri());

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
                .setHeader(Exchange.HTTP_QUERY, simple("itemUri=${header.MicoItemUri}&type=mico%3AImage&name=${header.CamelFileName}")) // &mimeType=${header.Content-Type}
                .to("http4:{{mico.api.server}}/broker/inject/add?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .unmarshal().json(JsonLibrary.Jackson, InjectAddResponse.class)
                .process(x -> {

                    // Record the part URI.
                    x.getIn().setHeader("MicoPartUri", ((InjectAddResponse) x.getIn().getBody()).getPartUri());
                    x.getIn().setBody(null);

                    // Pass over the message.
                    x.setOut(x.getIn());
                })
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.HTTP_QUERY, simple("item=${header.MicoItemUri}"))
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
                .setBody(simple("PREFIX mmm: <http://www.mico-project.eu/ns/mmm/2.0/schema#>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX oa: <http://www.w3.org/ns/oa#>\n" +
                        "\n" +
                        "SELECT DISTINCT ?o WHERE {\n" +
                        "  <${header.MicoItemUri}> mmm:hasPart [ \n" +
                        "    mmm:hasBody [ a <http://www.mico-project.eu/ns/mmmterms/2.0/schema#FaceDetectionBody> ] ;\n" +
                        "    mmm:hasTarget [ oa:hasSelector [ rdf:value ?o ] ]\n" +
                        "  ]" +
                        "}"))
                .to("http4:{{mico.api.server}}/marmotta/sparql/select?authUsername={{mico.api.username}}&authPassword={{mico.api.password}}")
                .to("direct:persist");

    }

}
