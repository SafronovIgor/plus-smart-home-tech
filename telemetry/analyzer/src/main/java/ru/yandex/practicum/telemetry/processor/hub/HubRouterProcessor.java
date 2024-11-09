package ru.yandex.practicum.telemetry.processor.hub;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.telemetry.entity.Action;
import ru.yandex.practicum.telemetry.entity.Scenario;
import ru.yandex.practicum.telemetry.entity.Sensor;

@Slf4j
@Service
public class HubRouterProcessor {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterProcessor(@Value("${grpc.client.hub-router.address}") String target) {
        this.hubRouterClient = HubRouterControllerGrpc.newBlockingStub(ManagedChannelBuilder
                .forTarget(target)
                .usePlaintext()
                .build()
        );
    }

    public void executeAction(Action action, String hubId, Scenario scenario, Sensor sensor) {
        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario.getName())
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(sensor.getId())
                        .setType(ActionTypeProto.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .build();

        try {
            hubRouterClient.handleDeviceAction(request);
        } catch (StatusRuntimeException e) {
            log.error("Error. Cannot send DeviceActionRequest", e);
        }
    }
}