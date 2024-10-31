package ru.yandex.practicum.telemetry.collector;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.configuration.KafkaConfig;

@Slf4j
@Getter
@Setter
@ToString
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaEventProducer {
    final KafkaProducer<String, SpecificRecordBase> producer;
    final KafkaConfig config;

    @Autowired
    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.config = kafkaConfig;
        this.producer = new KafkaProducer<>(kafkaConfig.getProducerProperties());
    }

    public void sendRecord(ProducerRecord<String, SpecificRecordBase> record) {
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error sending record with key={} and value={}", record.key(), record.value(), exception);
            } else {
                log.info("Sent record(key={} value={}) to partition={} with offset={}",
                        record.key(), record.value(), metadata.partition(), metadata.offset());
            }
        });
    }
}