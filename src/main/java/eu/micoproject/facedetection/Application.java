package eu.micoproject.facedetection;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The main entry point for the Spring Boot application.
 *
 * @since 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class Application {

    /**
     * The main function, called at start-up.
     *
     * @param args The command line arguments.
     * @since 1.0.0
     */
    public static void main(String[] args) {

        // Start-up the Spring Application, then get the Camel Spring Boot Application Controller in order to block the
        // main thread.
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        final CamelSpringBootApplicationController applicationController = context.getBean(CamelSpringBootApplicationController.class);
        applicationController.blockMainThread();

    }

}
