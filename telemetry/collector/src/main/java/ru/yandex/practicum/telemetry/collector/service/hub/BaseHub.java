package ru.yandex.practicum.telemetry.collector.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

public abstract class BaseHub implements HubService {
    KafkaEventProducer producer;
    String topic;

    public BaseHub(KafkaEventProducer kafkaProducer) {
        this.producer = kafkaProducer;
        this.topic = kafkaProducer.getConfig().getTopics().get("hubs-events");
    }

    @Override
    public void handle(HubEvent hubEvent) {
        var record = new ProducerRecord<>(
                topic,
                null,
                System.currentTimeMillis(),
                hubEvent.getHubId(),
                toAvro(hubEvent)
        );
        producer.sendRecord(record);
    }

    abstract SpecificRecordBase toAvro(HubEvent hubEvent);
}