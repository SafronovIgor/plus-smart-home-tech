package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.repository.SnapshotsRepo;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AggregatorSensorsSnapshotServiceImpl implements AggregatorSensorsSnapshotService {
    SnapshotsRepo snapshotsRepository;

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.debug("Updating state for event: {}", event);
        var oldSnapshot = snapshotsRepository.get(event.getHubId());

        if (oldSnapshot.isPresent()) {
            var oldEvent = Optional.ofNullable(oldSnapshot.get().getSensorsState().get(event.getId()));

            if (oldEvent.isPresent() && oldEvent.get().getTimestamp().isBefore(event.getTimestamp())) {
                var newSnapshot = oldSnapshot.get();
                newSnapshot.setTimestamp(event.getTimestamp());
                newSnapshot.getSensorsState().put(event.getId(), newSnapshot.getSensorsState().get(event.getId()));
                return Optional.of(newSnapshot);
            } else {
                return Optional.empty();
            }
        } else {
            var state = new HashMap<String, SensorStateAvro>();
            var sensorStateAvro = new SensorStateAvro();
            sensorStateAvro.setTimestamp(event.getTimestamp());
            sensorStateAvro.setData(event);
            state.put(event.getId(), sensorStateAvro);
            return Optional.ofNullable(snapshotsRepository.update(event.getHubId(),
                    SensorsSnapshotAvro.newBuilder()
                            .setHubId(event.getHubId())
                            .setTimestamp(Instant.now())
                            .setSensorsState(state)
                            .build()));
        }
    }

    @Override
    public void sendSnapshot(ProducerRecord<String, SensorsSnapshotAvro> snapshotRecord,
                             KafkaProducer<String, SensorsSnapshotAvro> producer) {
        log.debug("Sending snapshot: {}", snapshotRecord);
        try (producer) {
            producer.send(snapshotRecord);
            producer.flush();
            log.info("Snapshot sent successfully: {}", snapshotRecord);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("Failed to send snapshot: {}", snapshotRecord, e);
            throw new RuntimeException(e);
        }
    }
}