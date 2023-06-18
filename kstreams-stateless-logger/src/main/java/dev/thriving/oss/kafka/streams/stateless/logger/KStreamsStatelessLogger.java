package dev.thriving.oss.kafka.streams.stateless.logger;

import jakarta.enterprise.inject.Produces;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.jboss.logging.Logger;

public class KStreamsStatelessLogger {

    private static final Logger LOG = Logger.getLogger(KStreamsStatelessLogger.class);

    public static final String INPUT_TOPIC = "random-numbers";

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        final Serde<Long> longSerde = Serdes.Long();

        // Construct a `KStream` from the input topic
        final KStream<Long, Long> source = builder.stream(INPUT_TOPIC, Consumed.with(longSerde, longSerde));

        // print out messages received
//        source.peek((k, v) -> LOG.infof("received message => %s::%s", k, v));
        source.process(() -> new Processor<Long, Long, Long, Long>() {

            @Override
            public void process(Record<Long, Long> record) {
                LOG.debugf("received message => %s::%s", record.key(), record.value());
            }
        });

        return builder.build();
    }
}
