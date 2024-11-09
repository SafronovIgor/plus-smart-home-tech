package ru.yandex.practicum.telemetry.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
public class TransactionalService {

    @Transactional
    public <T> void transactionalMethod(Consumer<T> consumer, T t) {
        consumer.accept(t);
    }

    @Transactional
    public <T, U> void transactionalMethod(BiConsumer<T, U> consumer, T t, U u) {
        consumer.accept(t, u);
    }
}