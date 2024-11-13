package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.entity.ScenarioCondition;

import java.util.List;

public interface ScenarioConditionService {

    void add(ScenarioCondition obj);

    void remove(ScenarioCondition obj);

    void addAll(List<ScenarioCondition> list);
}