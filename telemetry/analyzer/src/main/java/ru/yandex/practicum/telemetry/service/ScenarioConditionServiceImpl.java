package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.telemetry.entity.ScenarioCondition;
import ru.yandex.practicum.telemetry.repository.ScenarioConditionRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScenarioConditionServiceImpl implements ScenarioConditionService {
    ScenarioConditionRepository scenarioConditionRepository;

    @Override
    public void add(ScenarioCondition scenarioCondition) {
        scenarioConditionRepository.save(scenarioCondition);
    }

    @Override
    public void remove(ScenarioCondition scenarioCondition) {
        scenarioConditionRepository.delete(scenarioCondition);
    }

    @Override
    public void addAll(List<ScenarioCondition> list) {
        scenarioConditionRepository.saveAll(list);
    }
}