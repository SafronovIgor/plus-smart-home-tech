package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.hub.HubService;
import ru.yandex.practicum.telemetry.collector.service.sensor.SensorService;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    Map<HubEventProto.PayloadCase, HubService> hubEventHandlerMap;
    Map<SensorEventProto.PayloadCase, SensorService> sensorEventHandlerMap;

    @Autowired
    public EventController(Set<HubService> hubEventHandler, Set<SensorService> sensorEventHandler) {
        this.hubEventHandlerMap = hubEventHandler.stream()
                .collect(Collectors.toMap(HubService::getMessageType, Function.identity()));
        this.sensorEventHandlerMap = sensorEventHandler.stream()
                .collect(Collectors.toMap(SensorService::getMessageType, Function.identity()));
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            hubEventHandlerMap.get(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            log.info("Successfully handled Hub event: {}", request);
        } catch (Exception e) {
            log.error("Error handling Hub event: {} - {}", request, e.getMessage(), e);
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage())
                    .withCause(e)
            ));
        }
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            sensorEventHandlerMap.get(request.getPayloadCase()).handle(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
            log.info("Successfully handled Sensor event: {}", request);
        } catch (Exception e) {
            log.error("Error handling Sensor event: {} - {}", request, e.getMessage(), e);
            responseObserver.onError(new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage())
                    .withCause(e)
            ));
        }
    }

}