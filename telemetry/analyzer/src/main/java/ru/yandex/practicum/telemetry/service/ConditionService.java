package ru.yandex.practicum.telemetry.service;

import ru.yandex.practicum.telemetry.entity.Condition;

import java.util.List;

public interface ConditionService {

    void add(Condition obj);

    void addAll(List<Condition> list);

    void remove(Condition obj);
}