package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.entity.Condition;
import ru.yandex.practicum.telemetry.repository.ConditionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConditionServiceImpl implements ConditionService {
    ConditionRepository conditionRepository;

    @Override
    public void add(Condition obj) {
        conditionRepository.save(obj);
    }

    @Override
    public void addAll(List<Condition> list) {
        conditionRepository.saveAll(list);
    }

    @Override
    public void remove(Condition obj) {
        conditionRepository.delete(obj);
    }
}