package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

@Service
public class MotionEvent extends BaseSensor {

    @Autowired
    public MotionEvent(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public MotionSensorAvro toAvro(SensorEventProto sensorEvent) {
        var motionEvent = sensorEvent.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setMotion(motionEvent.getMotion())
                .setLinkQuality(motionEvent.getLinkQuality())
                .setVoltage(motionEvent.getVoltage())
                .build();
    }
}