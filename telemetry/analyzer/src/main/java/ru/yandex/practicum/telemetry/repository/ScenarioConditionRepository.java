package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioCondition;
import ru.yandex.practicum.telemetry.entity.ScenarioConditionsId;
import ru.yandex.practicum.telemetry.entity.Sensor;

import java.util.Collection;
import java.util.List;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, ScenarioConditionsId> {

    List<ScenarioCondition> findBySensorAndScenarioIn(Sensor sensor, Collection<Scenario> scenarios);

    ScenarioCondition findBySensorAndScenario(Sensor s, Scenario scenario);

}