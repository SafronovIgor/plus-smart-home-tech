package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.telemetry.entity.Sensor;
import ru.yandex.practicum.telemetry.repository.SensorRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SensorServiceImpl implements SensorService {
    SensorRepository sensorRepository;

    @Override
    public void add(Sensor sensor) {
        sensorRepository.save(sensor);
    }

    @Override
    public void removeById(String sensorId) {
        sensorRepository.deleteById(sensorId);
    }

    @Override
    public Optional<Sensor> get(String sensorId, String hubId) {
        return sensorRepository.findByIdAndHubId(sensorId, hubId);
    }

    @Override
    public Sensor getById(String id) {
        return sensorRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }
}