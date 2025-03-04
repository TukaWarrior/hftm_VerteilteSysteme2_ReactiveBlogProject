package ch.hftm.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import ch.hftm.model.BlogDTO;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogConsumer {

    @Incoming("blogs-in") // Incoming Kafka channel
    @Blocking // Ensures processing happens on a worker thread
    public void consumeBlog(BlogDTO blogDTO) {
        System.out.println("Received blog for validation: " + blogDTO);

        // Simulate validation logic
        if (blogDTO.getTitle() != null && !blogDTO.getTitle().isEmpty()) {
            System.out.println("Blog validated successfully: " + blogDTO);
        } else {
            System.out.println("Blog validation failed: " + blogDTO);
        }
    }
}