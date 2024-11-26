package ru.yandex.practicum.telemetry.handler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.entity.*;
import ru.yandex.practicum.telemetry.repository.ScenarioActionRepository;
import ru.yandex.practicum.telemetry.repository.ScenarioConditionRepository;
import ru.yandex.practicum.telemetry.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.repository.SensorRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SnapshotHandler {
    ScenarioRepository scenarioRepository;
    SensorRepository sensorRepository;
    ScenarioConditionRepository scenarioConditionRepository;
    ScenarioActionRepository scenarioActionRepository;
    //HubRouterProcessor hubRouterProcessor; используется локально для проверки результата

    public void accept(SensorsSnapshotAvro snapshot) {
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        sensorsState.keySet().forEach(sensorId -> sensorRepository.findById(sensorId).ifPresent(sensor -> {
            scenarioRepository.findByHubId(snapshot.getHubId()).forEach(scenario -> {
                ScenarioCondition condition = scenarioConditionRepository.findBySensorAndScenario(sensor, scenario);
                SensorStateAvro sensorStateAvro = sensorsState.get(sensorId);
                ScenarioAction action = scenarioActionRepository.findBySensorAndScenario(sensor, scenario);
                if (isConditionTriggered(condition, sensorStateAvro)) {
                    //hubRouterProcessor.executeAction(action.getAction(), snapshot.getHubId(), scenario, sensor);
                }
            });
        }));
    }

    private boolean isConditionTriggered(ScenarioCondition scenarioCondition, SensorStateAvro sensorState) {
        Condition condition = scenarioCondition.getCondition();
        ConditionType conditionType = condition.getType();

        switch (conditionType) {
            case MOTION -> {
                MotionSensorAvro motionSensorAvro = (MotionSensorAvro) sensorState.getData();
                int motionValue = motionSensorAvro.getMotion() ? 1 : 0;
                return isConditionCompare(motionValue, condition.getOperation(), condition.getValue());
            }
            case LUMINOSITY -> {
                LigntSensorAvro lightSensor = (LigntSensorAvro) sensorState.getData();
                return isConditionCompare(lightSensor.getLinkQuality(), condition.getOperation(), condition.getValue());
            }
            case SWITCH -> {
                SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorState.getData();
                int switchStatus = switchSensor.getState() ? 1 : 0;
                return isConditionCompare(switchStatus, condition.getOperation(), condition.getValue());
            }
            case TEMPERATURE -> {
                TemparatureSensorAvro temperatureSensor = (TemparatureSensorAvro) sensorState.getData();
                return isConditionCompare(temperatureSensor.getTemparatureC(),
                        condition.getOperation(),
                        condition.getValue());
            }
            case CO2LEVEL -> {
                ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
                return isConditionCompare(climateSensor.getCo2Level(), condition.getOperation(), condition.getValue());
            }
            case HUMIDITY -> {
                ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
                return isConditionCompare(climateSensor.getHumidity(), condition.getOperation(), condition.getValue());
            }
            default -> {
                return false;
            }
        }
    }

    private boolean isConditionCompare(int sensorValue, ConditionOperation operation, int conditionValue) {
        return switch (operation) {
            case EQUALS -> sensorValue == conditionValue;
            case GREATER_THAN -> sensorValue > conditionValue;
            case LOWER_THAN -> sensorValue < conditionValue;
        };
    }
}