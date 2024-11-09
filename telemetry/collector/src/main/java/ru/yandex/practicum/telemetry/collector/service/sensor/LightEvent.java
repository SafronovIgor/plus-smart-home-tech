package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LigntSensorAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

@Service
public class LightEvent extends BaseSensor {

    @Autowired
    public LightEvent(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public LigntSensorAvro toAvro(SensorEventProto sensorEvent) {
        var lightEvent = sensorEvent.getLightSensorEvent();
        return LigntSensorAvro.newBuilder()
                .setLinkQuality(lightEvent.getLinkQuality())
                .setLuminosity(lightEvent.getLuminosity())
                .build();
    }
}