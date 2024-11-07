package ru.yandex.practicum.telemetry.collector.service.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorService {

    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto sensorEvent);
}