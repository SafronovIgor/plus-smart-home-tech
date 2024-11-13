package ru.yandex.practicum.telemetry.processor.snapshot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.configuration.KafkaConfig;
import ru.yandex.practicum.telemetry.handler.SnapshotHandler;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SnapshotProcessorImpl implements SnapshotProcessor {
    KafkaConfig kafkaConfig;
    KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    SnapshotHandler snapshotHandler;

    @Autowired
    public SnapshotProcessorImpl(KafkaConfig kafkaConfig, SnapshotHandler snapshotHandler) {
        this.kafkaConfig = kafkaConfig;
        this.snapshotHandler = snapshotHandler;
        this.consumer = new KafkaConsumer<>(kafkaConfig.getConsumerProperties());
    }

    @Override
    public void start() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(kafkaConfig.getTopics().get("sensors-snapshot")));
            while (true) {
                var records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, SensorsSnapshotAvro> rec : records) {
                    var snapshot = rec.value();
                    snapshotHandler.accept(snapshot);
                    log.info("Received snapshot : {}", snapshot);
                }

                consumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        log.warn("Commit processing error. Offsets: {}", offsets, exception);
                    }
                });
            }
        } catch (WakeupException e) {
            log.warn("WakeupException caught, shutting down consumer: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception caught in start(): {}", e.getMessage(), e);
        }
    }
}