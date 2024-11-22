package ru.yandex.practicum.telemetry.handler;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.entity.*;
import ru.yandex.practicum.telemetry.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectorHandler {

    Map<Class<?>, Consumer<Object>> handlers = new HashMap<>();
    Map<Class<?>, BiConsumer<HubEventAvro, Object>> eventHandlerMap = new HashMap<>();

    TransactionalService transactionalService;
    ScenarioService scenarioService;
    ConditionService conditionService;
    ActionService actionService;
    SensorService sensorService;
    ScenarioConditionService scenarioConditionService;
    ScenarioActionService scenarioActionService;

    @Autowired
    public CollectorHandler(TransactionalService transactionalService,
                            ScenarioService scenarioService,
                            ConditionService conditionService,
                            ActionService actionService,
                            SensorService sensorService,
                            ScenarioConditionService scenarioConditionService,
                            ScenarioActionService scenarioActionService) {
        this.transactionalService = transactionalService;
        this.scenarioService = scenarioService;
        this.conditionService = conditionService;
        this.actionService = actionService;
        this.sensorService = sensorService;
        this.scenarioConditionService = scenarioConditionService;
        this.scenarioActionService = scenarioActionService;

        handlers.put(HubEventAvro.class, this::handleHubEventAvro);

        eventHandlerMap.put(ScenarioAddedEventAvro.class,
                (event, payload) -> handleScenarioAddedEventAvro(event, (ScenarioAddedEventAvro) payload));
        eventHandlerMap.put(DeviceAddedEventAvro.class,
                (event, payload) -> handleDeviceAddedEventAvro(event, (DeviceAddedEventAvro) payload));
        eventHandlerMap.put(DeviceRemovedEventAvro.class,
                (event, payload) -> handleDeviceRemovedEventAvro(event, (DeviceRemovedEventAvro) payload));
        eventHandlerMap.put(ScenarioRemovedEventAvro.class,
                (event, payload) -> handleScenarioRemovedEventAvro(event, (ScenarioRemovedEventAvro) payload));
    }

    public <T> void handle(T object) {
        Objects.requireNonNull(object, "Input object cannot be null.");
        Consumer<Object> handler = handlers.get(object.getClass());

        if (handler != null) {
            log.info("Handling object of type: {}", object.getClass().getName());
            handler.accept(object);
        } else {
            String errorMsg = "No handler found for type: " + object.getClass().getName();
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    private void handleHubEventAvro(Object object) {
        HubEventAvro event = (HubEventAvro) object;
        Object payload = event.getPayload();
        var handler = eventHandlerMap.get(payload.getClass());

        if (handler != null) {
            log.info("Handling payload of type: {}", payload.getClass().getName());
            handler.accept(event, payload);
        } else {
            log.warn("No handler found for payload type: {}", payload.getClass().getName());
        }
    }

    private void handleScenarioAddedEventAvro(HubEventAvro event, ScenarioAddedEventAvro payload) {
        log.info("Processing ScenarioAddedEventAvro: {}", payload);
        transactionalService.transactionalMethod((e, p) -> {
            var scenario = scenarioService.add(Scenario.builder()
                    .hubId(e.getHubId())
                    .name(p.getName())
                    .build());

            var conditionList = new ArrayList<Condition>();
            var scenarioConditionList = new ArrayList<ScenarioCondition>();

            p.getConditions().forEach(cond -> {
                var condition = Condition.builder()
                        .type(ConditionType.valueOf(cond.getType().name()))
                        .operation(ConditionOperation.valueOf(cond.getOperation().name()))
                        .value((Integer) cond.getValue())
                        .build();
                var scenarioCondition = ScenarioCondition.builder()
                        .sensor(sensorService.getById(cond.getSensorId()))
                        .scenario(scenario)
                        .condition(condition)
                        .build();
                conditionList.add(condition);
                scenarioConditionList.add(scenarioCondition);
            });

            conditionService.addAll(conditionList);
            scenarioConditionService.addAll(scenarioConditionList);

            var actionList = new ArrayList<Action>();
            var scenarioActionList = new ArrayList<ScenarioAction>();

            p.getAction().forEach(a -> {
                var action = Action.builder()
                        .type(ActionType.valueOf(a.getType().name()))
                        .value(a.getValue())
                        .build();
                var scenarioAction = ScenarioAction.builder()
                        .scenario(scenario)
                        .sensor(sensorService.getById(a.getSensorId()))
                        .action(action)
                        .build();
                actionList.add(action);
                scenarioActionList.add(scenarioAction);
            });

            actionService.addAll(actionList);
            scenarioActionService.addAll(scenarioActionList);
        }, event, payload);
    }

    private void handleDeviceAddedEventAvro(HubEventAvro event, DeviceAddedEventAvro payload) {
        log.info("Processing DeviceAddedEventAvro: {} for hub: {}", payload, event.getHubId());
        transactionalService.transactionalMethod((e, p) -> sensorService.add(Sensor.builder()
                .id(p.getId())
                .hubId(e.getHubId())
                .build()), event, payload);
    }

    private void handleDeviceRemovedEventAvro(HubEventAvro event, DeviceRemovedEventAvro payload) {
        log.info("Processing DeviceRemovedEventAvro: {} for hub: {}", payload, event.getHubId());
        transactionalService.transactionalMethod(p -> {
            log.debug("Removing sensor with id: {}", p.getId());
            sensorService.removeById(p.getId());
        }, payload);
    }

    private void handleScenarioRemovedEventAvro(HubEventAvro event, ScenarioRemovedEventAvro payload) {
        log.info("Processing ScenarioRemovedEventAvro: {} for hub: {}", payload, event.getHubId());
        transactionalService.transactionalMethod(p -> {
            log.debug("Removing scenario with name: {}", p.getName());
            scenarioService.removeByName(p.getName());
        }, payload);
    }
}