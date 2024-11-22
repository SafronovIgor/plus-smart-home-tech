package ru.yandex.practicum.telemetry.service;


import ru.yandex.practicum.telemetry.entity.Action;

import java.util.List;

public interface ActionService {
    void add(Action obj);

    void addAll(List<Action> list);

    void remove(Action obj);
}