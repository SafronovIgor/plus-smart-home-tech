package ru.yandex.practicum.telemetry.collector.service.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.KafkaEventProducer;

import java.time.Instant;

@Service
public class ScenarioAdded extends BaseHub {

    @Autowired
    public ScenarioAdded(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubEventAvro toAvro(HubEventProto hubEvent) {
        var addedScenarioEvent = hubEvent.getScenarioAdded();
        var actionAvroList = addedScenarioEvent.getActionList().stream()
                .map(this::toDeviceActionAvro)
                .toList();
        var scenarioConditionAvroList = addedScenarioEvent.getConditionList().stream()
                .map(this::toScenarioConditionAvro)
                .toList();
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(hubEvent.getTimestamp().getSeconds()))
                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(addedScenarioEvent.getName())
                        .setAction(actionAvroList)
                        .setConditions(scenarioConditionAvroList)
                        .build())
                .build();
    }

    private DeviceActionAvro toDeviceActionAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    private ActionTypeAvro toActionTypeAvro(ActionTypeProto actionType) {
        return switch (actionType) {
            case ActionTypeProto.ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case ActionTypeProto.DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case ActionTypeProto.INVERSE -> ActionTypeAvro.INVERSE;
            case ActionTypeProto.SET_VALUE -> ActionTypeAvro.SET_VALUE;
            default -> null;
        };
    }

    private ScenarioConditionAvro toScenarioConditionAvro(ScenarioConditionProto scenarioCondition) {
        Object value;
        var valueCase = scenarioCondition.getValueCase();

        if (valueCase == ScenarioConditionProto.ValueCase.INT_VALUE) {
            value = scenarioCondition.getIntValue();
        } else {
            value = scenarioCondition.getBoolValue();
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(toConditionTypeAvro(scenarioCondition.getType()))
                .setValue(value)
                .setOperation(toConditionOperationAvro(scenarioCondition.getOperation()))
                .build();
    }

    private ConditionTypeAvro toConditionTypeAvro(ConditionTypeProto conditionType) {
        return switch (conditionType) {
            case ConditionTypeProto.MOTION -> ConditionTypeAvro.MOTION;
            case ConditionTypeProto.LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case ConditionTypeProto.SWITCH -> ConditionTypeAvro.SWITCH;
            case ConditionTypeProto.TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case ConditionTypeProto.CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case ConditionTypeProto.HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            default -> null;
        };
    }

    private ConditionOperationAvro toConditionOperationAvro(ConditionOperationProto conditionOperation) {
        return switch (conditionOperation) {
            case ConditionOperationProto.EQUALS -> ConditionOperationAvro.EQUALS;
            case ConditionOperationProto.GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case ConditionOperationProto.LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            default -> null;
        };
    }
}