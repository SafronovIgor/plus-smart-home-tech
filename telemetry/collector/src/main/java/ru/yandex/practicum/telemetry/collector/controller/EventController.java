package ru.yandex.practicum.telemetry.collector.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.hub.HubService;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorService;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = "/events", consumes = APPLICATION_JSON_VALUE)
public class EventController {
    private final Map<HubEventType, HubService> hubEventHandlerMap;
    private final Map<SensorEventType, SensorService> sensorEventHandlerMap;

    @Autowired
    public EventController(Set<HubService> hubEventHandlerSet,
                           Set<SensorService> sensorEventHandlerSet) {
        this.hubEventHandlerMap = hubEventHandlerSet.stream()
                .collect(Collectors.toMap(HubService::getMessageType, Function.identity()));
        this.sensorEventHandlerMap = sensorEventHandlerSet.stream()
                .collect(Collectors.toMap(SensorService::getMessageType, Function.identity()));
    }
}
