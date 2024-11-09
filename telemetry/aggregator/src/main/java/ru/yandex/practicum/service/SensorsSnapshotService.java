package ru.yandex.practicum.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface SensorsSnapshotService {

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);

    void sendSnapshot(ProducerRecord<String, SensorsSnapshotAvro> rec,
                      KafkaProducer<String, SensorsSnapshotAvro> producer);
}