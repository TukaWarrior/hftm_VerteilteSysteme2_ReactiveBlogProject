package ch.hftm;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessagingTesting {

    @Outgoing("source")
    public Multi<String> source() {
        return Multi.createFrom().items("hallo", "böse", "quarkus", "fans");
    }

    @Incoming("source")
    @Outgoing("processed-a")
    public String toUpperCase(String payload) {
        return payload.toUpperCase();
    }

    @Incoming("processed-a")
    @Outgoing("processed-b")
    public String replaceEvilWithLove(String payload) {
        if ("BÖSE".equals(payload)) {
            return "LOVE";
        }
        return payload;
    }

    @Incoming("processed-b")
    @Outgoing("processed-c")
    public Multi<String> filterShortStrings(Multi<String> payload) {
        return payload.select().where(word -> word.length() >= 6);
    }



    @Incoming("processed-c")
    public void sink(String word) {
        System.out.println(">> " + word);
    }
    
}