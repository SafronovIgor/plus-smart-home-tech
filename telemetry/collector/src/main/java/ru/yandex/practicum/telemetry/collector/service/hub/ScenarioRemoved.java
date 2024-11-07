package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

@Service
public class ScenarioRemoved extends BaseHub {

    public ScenarioRemoved(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public ScenarioRemovedEventAvro toAvro(HubEventProto hubEvent) {
        var event = hubEvent.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}