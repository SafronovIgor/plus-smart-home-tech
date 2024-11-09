package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.entity.Scenario;

public interface ScenarioService {

    Scenario add(Scenario obj);

    void removeByName(String nameObj);
}