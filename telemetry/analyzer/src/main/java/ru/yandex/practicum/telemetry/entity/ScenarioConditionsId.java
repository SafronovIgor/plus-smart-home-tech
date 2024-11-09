package ru.yandex.practicum.telemetry.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioConditionsId implements Serializable {

    Long scenario;

    String sensor;

    Long condition;
}