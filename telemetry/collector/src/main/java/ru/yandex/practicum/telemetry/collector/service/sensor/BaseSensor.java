package ru.yandex.practicum.telemetry.collector.service.sensor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseSensor implements SensorService {
    KafkaEventProducer producer;
    String topic;

    @Autowired
    protected BaseSensor(KafkaEventProducer kafkaProducer) {
        this.producer = kafkaProducer;
        topic = kafkaProducer.getConfig().getTopics().get("sensors-events");
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        var producerRecord = new ProducerRecord<>(
                topic,
                null,
                System.currentTimeMillis(),
                sensorEvent.getHubId(),
                toAvro(sensorEvent)
        );
        producer.sendRecord(producerRecord);
    }

    protected abstract SpecificRecordBase toAvro(SensorEvent sensorEvent);
}