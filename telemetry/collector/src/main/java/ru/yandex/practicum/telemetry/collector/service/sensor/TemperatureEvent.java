package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemparatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

import java.time.Instant;

@Service
public class TemperatureEvent extends BaseSensor {

    @Autowired
    public TemperatureEvent(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SensorEventAvro toAvro(SensorEventProto sensorEvent) {
        var temperatureEvent = sensorEvent.getTemperatureSensorEvent();
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(sensorEvent.getTimestamp().getSeconds()))
                .setPayload(TemparatureSensorAvro.newBuilder()
                        .setTemperatureF(temperatureEvent.getTemperatureF())
                        .setTemparatureC(temperatureEvent.getTemperatureC())
                        .build())
                .build();
    }
}