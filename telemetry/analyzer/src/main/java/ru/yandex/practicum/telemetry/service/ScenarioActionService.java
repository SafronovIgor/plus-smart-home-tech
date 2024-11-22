package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.entity.Action;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioAction;
import ru.yandex.practicum.telemetry.entity.Sensor;

import java.util.List;

public interface ScenarioActionService {
    void add(Scenario obj, Sensor obj2, Action obj3);

    void addAll(List<ScenarioAction> list);

    void remove(Scenario obj, Sensor obj2, Action obj3);
}