package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioAction;
import ru.yandex.practicum.telemetry.entity.ScenarioActionsId;
import ru.yandex.practicum.telemetry.entity.Sensor;

@Repository
public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionsId> {

    ScenarioAction findBySensorAndScenario(Sensor sensor, Scenario scenario);

}