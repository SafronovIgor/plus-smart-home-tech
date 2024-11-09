package ru.yandex.practicum.telemetry.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.configuration.KafkaConfig;
import ru.yandex.practicum.telemetry.aggregator.service.SensorsSnapshotService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AggregatorStarter {
    private final KafkaConfig kafkaConfig;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final SensorsSnapshotService sensorsSnapshotService;

    @Autowired
    public AggregatorStarter(KafkaConfig kafkaConfig, SensorsSnapshotService sensorsSnapshotService) {
        this.kafkaConfig = kafkaConfig;
        this.producer = new KafkaProducer<>(kafkaConfig.getProducerProperties());
        this.consumer = new KafkaConsumer<>(kafkaConfig.getConsumerProperties());
        this.sensorsSnapshotService = sensorsSnapshotService;
    }

    public void start() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(kafkaConfig.getTopics().get("sensors-events")));

            while (true) {
                var records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, SensorEventAvro> rec : records) {
                    var event = rec.value();
                    var updateSnapshot = updateState(event);
                    updateSnapshot.ifPresent(this::sendSnapshot);
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
        } finally {
            consumer.close();
            log.info("Consumer closed at {}", Instant.now());

            producer.close();
            log.info("Producer closed at {}", Instant.now());
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        return sensorsSnapshotService.updateState(event);
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        log.debug("Sending snapshot: {}", snapshot);
        sensorsSnapshotService.sendSnapshot(
                new ProducerRecord<>(
                        kafkaConfig.getTopics().get("sensors-snapshots"),
                        null,
                        snapshot.getHubId(), snapshot
                ), producer
        );
    }
}