package ru.yandex.practicum.telemetry.aggregator.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SnapshotsRepo {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> get(String id) {
        return Optional.ofNullable(snapshots.get(id));
    }

    public SensorsSnapshotAvro update(String id, SensorsSnapshotAvro value) {
        return snapshots.put(id, value);
    }
}