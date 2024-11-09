package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioCondition;
import ru.yandex.practicum.telemetry.entity.ScenarioConditionsId;
import ru.yandex.practicum.telemetry.entity.Sensor;

import java.util.Collection;
import java.util.List;

@Repository
public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, ScenarioConditionsId> {

    List<ScenarioCondition> findBySensorAndScenarioIn(Sensor sensor, Collection<Scenario> scenarios);

    ScenarioCondition findBySensorAndScenario(Sensor s, Scenario scenario);

}