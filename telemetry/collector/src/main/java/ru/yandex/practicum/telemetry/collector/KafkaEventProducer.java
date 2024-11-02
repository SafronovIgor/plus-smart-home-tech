package ru.yandex.practicum.telemetry.collector;

import jakarta.annotation.PreDestroy;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaEventProducer {
    KafkaProducer<String, SpecificRecordBase> producer;
    KafkaConfig config;

    @Autowired
    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.config = kafkaConfig;
        this.producer = new KafkaProducer<>(kafkaConfig.getProducerProperties());
    }

    public void sendRecord(ProducerRecord<String, SpecificRecordBase> producerRecord) {
        producer.send(producerRecord, (metadata, exception) -> {
            if (exception != null) {
                log.error("Error sending producerRecord with key={} and value={}",
                        producerRecord.key(), producerRecord.value(), exception);
            } else {
                log.info("Sent producerRecord(key={} value={}) to partition={} with offset={}",
                        producerRecord.key(), producerRecord.value(), metadata.partition(), metadata.offset());
            }
        });
    }

    @PreDestroy
    public void close() {
        producer.close();
    }
}