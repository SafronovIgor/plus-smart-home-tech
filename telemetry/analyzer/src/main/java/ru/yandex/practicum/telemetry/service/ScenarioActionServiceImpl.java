package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.entity.Action;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.ScenarioAction;
import ru.yandex.practicum.telemetry.entity.Sensor;
import ru.yandex.practicum.telemetry.repository.ScenarioActionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScenarioActionServiceImpl implements ScenarioActionService {
    ScenarioActionRepository scenarioActionRepository;

    @Override
    public void add(Scenario scenario, Sensor sensor, Action action) {
        scenarioActionRepository.save(ScenarioAction.builder()
                .scenario(scenario)
                .sensor(sensor)
                .action(action)
                .build());
    }

    @Override
    public void remove(Scenario scenario, Sensor sensor, Action action) {
        scenarioActionRepository.delete(ScenarioAction.builder()
                .scenario(scenario)
                .sensor(sensor)
                .action(action)
                .build());
    }

    @Override
    public void addAll(List<ScenarioAction> list) {
        scenarioActionRepository.saveAll(list);
    }
}