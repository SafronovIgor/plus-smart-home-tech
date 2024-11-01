package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioRemovedEvent;

@Service
public class ScenarioRemoved extends BaseHub {

    public ScenarioRemoved(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }


    @Override
    ScenarioRemovedEventAvro toAvro(HubEvent hubEvent) {
        var event = (ScenarioRemovedEvent) hubEvent;

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }
}