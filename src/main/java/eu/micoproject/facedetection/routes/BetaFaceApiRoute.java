package eu.micoproject.facedetection.routes;

import eu.micoproject.facedetection.model.Image;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageInfoResponse;
import eu.micoproject.facedetection.model.betafaceapi.BetafaceImageResponse;
import eu.micoproject.facedetection.model.betafaceapi.ImageInfoRequestUid;
import eu.micoproject.facedetection.model.betafaceapi.ImageRequestBinary;
import eu.micoproject.facedetection.repo.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A route to process images with the BetaFaceApi service.
 *
 * @since 1.0.0
 */
@Slf4j
@Component
public class BetaFaceApiRoute extends RouteBuilder {

    /**
     * Configure the route.
     *
     * @throws Exception
     * @since 1.0.0
     */
    @Override
    public void configure() throws Exception {

        from("direct:betafaceapi.upload_new_image_file")
                .convertBodyTo(ImageRequestBinary.class)
                .marshal().jacksonxml()
                .setHeader(Exchange.CONTENT_TYPE, constant("application/xml"))
                .to("http4:www.betafaceapi.com/service.svc/UploadNewImage_File")
                .unmarshal().jacksonxml(BetafaceImageResponse.class)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to("direct:betafaceapi.get_image_info");

        from("direct:betafaceapi.get_image_info")
                .convertBodyTo(ImageInfoRequestUid.class).marshal().json(JsonLibrary.Jackson)
                .to("http4:www.betafaceapi.com/service_json.svc/GetImageInfo")
                .unmarshal().json(JsonLibrary.Jackson, BetafaceImageInfoResponse.class)
                .to("log:responses")
                // Set the response code as a Camel header.
                .process(x -> {
                    final BetafaceImageInfoResponse response = (BetafaceImageInfoResponse) x.getIn().getBody();
                    x.getIn().setHeader("betafaceapi.response.code", response.getCode());
                    x.setOut(x.getIn());
                })
                .choice()
                .when(header("betafaceapi.response.code").isEqualTo(0)).to("direct:persist")
                .when(header("betafaceapi.response.code").isEqualTo(1)).to("direct:betafaceapi.get_image_info")
                .otherwise().to("log:dead_end");

    }

}
