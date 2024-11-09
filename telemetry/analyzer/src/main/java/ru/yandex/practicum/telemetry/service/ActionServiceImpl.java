package ru.yandex.practicum.telemetry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.entity.Action;
import ru.yandex.practicum.telemetry.repository.ActionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActionServiceImpl implements ActionService {
    ActionRepository actionRepository;

    @Override
    public void add(Action obj) {
        actionRepository.save(obj);
    }

    @Override
    public void addAll(List<Action> list) {
        actionRepository.saveAll(list);
    }

    @Override
    public void remove(Action obj) {
        actionRepository.delete(obj);
    }
}