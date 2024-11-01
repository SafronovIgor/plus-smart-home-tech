package ru.yandex.practicum.telemetry.collector.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

public abstract class BaseHub implements HubService {
    KafkaEventProducer producer;
    String eventTopic;

    protected BaseHub(KafkaEventProducer kafkaProducer) {
        this.producer = kafkaProducer;
        this.eventTopic = kafkaProducer.getConfig().getTopics().get("hubs-events");
    }

    @Override
    public void handle(HubEvent hubEvent) {
        var producerRecord = new ProducerRecord<>(
                eventTopic,
                null,
                System.currentTimeMillis(),
                hubEvent.getHubId(),
                toAvro(hubEvent)
        );
        producer.sendRecord(producerRecord);
    }

    abstract SpecificRecordBase toAvro(HubEvent hubEvent);
}