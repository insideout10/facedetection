package eu.micoproject.facedetection.routes;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public void configure() throws Exception {

        from("file://{{facedetection.inbox}}")
                .to("direct:mico.upload_new_image_file");
//                .to("direct:betafaceapi.upload_new_image_file");

        from("direct:persist")
                // Need to handle a response in progress (checksum = null)
                .convertBodyTo(Image.class)
                .process(exchange -> {

                    final Image image = (Image) exchange.getIn().getBody();
                    imageRepository.save(image);

                })
                .to("mock:foo");

    }

}
