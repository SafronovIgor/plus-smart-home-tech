package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;

public abstract class BaseSensor implements SensorService {
    KafkaEventProducer producer;
    String topic;

    public BaseSensor(KafkaEventProducer kafkaProducer) {
        this.producer = kafkaProducer;
        topic = kafkaProducer.getConfig().getTopics().get("sensors-events");
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        var record = new ProducerRecord<>(
                topic,
                null,
                System.currentTimeMillis(),
                sensorEvent.getHubId(),
                toAvro(sensorEvent)
        );
        producer.sendRecord(record);
    }

    abstract SpecificRecordBase toAvro(SensorEvent sensorEvent);

}
