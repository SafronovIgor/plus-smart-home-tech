package ru.yandex.practicum.telemetry.collector.service.hub;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

import java.time.Instant;

@Service
public class DeviceRemoved extends BaseHub {

    public DeviceRemoved(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto hubEvent) {
        var addedDeviceEvent = hubEvent.getDeviceRemoved();
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(hubEvent.getTimestamp().getSeconds()))
                .setPayload(DeviceRemovedEventAvro.newBuilder()
                        .setId(addedDeviceEvent.getId())
                        .build())
                .build();
    }
}