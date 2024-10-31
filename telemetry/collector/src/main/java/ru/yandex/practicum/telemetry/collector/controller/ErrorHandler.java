package ru.yandex.practicum.telemetry.collector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.telemetry.collector.configuration.ConstantsConfig;
import ru.yandex.practicum.telemetry.collector.model.ErrorAPI;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {
    private final ConstantsConfig constantsConfig;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorAPI handleException(final Exception e) {
        log.warn("Unknown error.", e);
        return ErrorAPI.builder()
                .error(constantsConfig.getDefaultErrorName())
                .description(e.getMessage())
                .build();
    }
}