package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
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
    public EventController(Set<HubService> hubEventHandler,
                           Set<SensorService> sensorEventHandler) {
        this.hubEventHandlerMap = hubEventHandler.stream()
                .collect(Collectors.toMap(HubService::getMessageType, Function.identity()));
        this.sensorEventHandlerMap = sensorEventHandler.stream()
                .collect(Collectors.toMap(SensorService::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void addSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        if (sensorEventHandlerMap.containsKey(sensorEvent.getType())) {
            sensorEventHandlerMap.get(sensorEvent.getType()).handle(sensorEvent);
        } else {
            throw new IllegalArgumentException("Handler for sensor type event " + sensorEvent.getType() + " not found");
        }
    }

    @PostMapping("/hubs")
    public void addHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        if (hubEventHandlerMap.containsKey(hubEvent.getType())) {
            hubEventHandlerMap.get(hubEvent.getType()).handle(hubEvent);
        } else {
            throw new IllegalArgumentException("Handler for hub type event " + hubEvent.getType() + " not found");
        }
    }
}