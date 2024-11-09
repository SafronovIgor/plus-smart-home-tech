package ru.yandex.practicum.telemetry.collector.service.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubService {

    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto hubEvent);
}