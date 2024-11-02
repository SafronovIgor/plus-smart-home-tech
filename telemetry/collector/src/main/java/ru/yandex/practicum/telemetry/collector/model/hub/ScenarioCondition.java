package ru.yandex.practicum.telemetry.collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.telemetry.collector.model.enums.ConditionOperation;
import ru.yandex.practicum.telemetry.collector.model.enums.ConditionType;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    @NotBlank
    String sensorId;
    @NotNull
    ConditionType type;
    @NotNull
    ConditionOperation operation;
    int value;
}