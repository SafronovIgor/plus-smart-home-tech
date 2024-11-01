package ru.yandex.practicum.telemetry.collector.service.hub;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;

@Service
public class DeviceRemoved extends BaseHub {

    public DeviceRemoved(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    DeviceRemovedEventAvro toAvro(HubEvent hubEvent) {
        var addedDeviceEvent = (DeviceAddedEvent) hubEvent;

        return DeviceRemovedEventAvro.newBuilder()
                .setId(addedDeviceEvent.getId())
                .build();
    }
}