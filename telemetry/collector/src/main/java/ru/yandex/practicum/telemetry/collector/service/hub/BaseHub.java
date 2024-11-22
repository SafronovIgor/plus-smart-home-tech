package ru.yandex.practicum.telemetry.collector.service.hub;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class BaseHub implements HubService {
    KafkaEventProducer producer;
    String eventTopic;

    @Autowired
    protected BaseHub(KafkaEventProducer kafkaProducer) {
        this.producer = kafkaProducer;
        this.eventTopic = kafkaProducer.getConfig().getTopics().get("hubs-events");
    }

    @Override
    public void handle(HubEventProto hubEvent) {
        var producerRecord = new ProducerRecord<>(
                eventTopic,
                null,
                System.currentTimeMillis(),
                hubEvent.getHubId(),
                toAvro(hubEvent)
        );
        producer.sendRecord(producerRecord);
    }

    protected abstract SpecificRecordBase toAvro(HubEventProto hubEvent);
}