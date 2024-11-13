package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.entity.Sensor;

import java.util.Optional;

public interface SensorService {

    void add(Sensor obj);

    void removeById(String objId);

    Optional<Sensor> get(String id, String hubId);

    Sensor getById(String id);
}