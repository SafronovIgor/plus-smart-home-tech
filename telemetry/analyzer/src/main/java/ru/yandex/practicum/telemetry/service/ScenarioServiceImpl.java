package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.repository.ScenarioRepository;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScenarioServiceImpl implements ScenarioService {
    ScenarioRepository scenarioRepository;

    @Override
    public Scenario add(Scenario obj) {
        return scenarioRepository.save(obj);
    }

    @Override
    public void removeByName(String scenarioName) {
        scenarioRepository.deleteByName(scenarioName);
    }
}