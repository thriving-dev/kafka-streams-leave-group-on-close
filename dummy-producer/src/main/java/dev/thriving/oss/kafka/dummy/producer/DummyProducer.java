package dev.thriving.oss.kafka.dummy.producer;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.Random;

/**
 * source: <a href="https://quarkus.io/guides/kafka-streams">https://quarkus.io/guides/kafka-streams</a>
 */
@ApplicationScoped
public class DummyProducer {

    private static final Logger LOG = Logger.getLogger(DummyProducer.class);

    private final Random random = new Random();

    @Outgoing("random-numbers")
    public Multi<Record<Long, Long>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(10))
                .onOverflow().drop()
                .map(tick -> {
                    Long num = random.nextLong(100_000);
                    LOG.infof("emit => %s::%s", num, num);
                    return Record.of(num, num);
                });
    }
}
