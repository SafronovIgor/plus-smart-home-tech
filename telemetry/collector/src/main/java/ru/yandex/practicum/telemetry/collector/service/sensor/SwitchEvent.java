package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

@Service
public class SwitchEvent extends BaseSensor {

    @Autowired
    public SwitchEvent(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SwitchSensorAvro toAvro(SensorEventProto sensorEvent) {
        var switchEvent = sensorEvent.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(switchEvent.getState())
                .build();
    }
}