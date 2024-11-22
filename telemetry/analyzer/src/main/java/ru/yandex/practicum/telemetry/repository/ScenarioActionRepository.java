package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioAction;
import ru.yandex.practicum.telemetry.entity.ScenarioActionsId;
import ru.yandex.practicum.telemetry.entity.Sensor;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionsId> {

    ScenarioAction findBySensorAndScenario(Sensor sensor, Scenario scenario);

}