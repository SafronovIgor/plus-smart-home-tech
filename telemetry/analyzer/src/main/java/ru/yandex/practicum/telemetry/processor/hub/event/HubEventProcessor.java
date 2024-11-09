package ru.yandex.practicum.telemetry.processor.hub.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.configuration.KafkaConfig;
import ru.yandex.practicum.telemetry.handler.CollectorHandler;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConfig kafkaConfig;
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final CollectorHandler collectorHandler;

    @Autowired
    public HubEventProcessor(KafkaConfig kafkaConfig, CollectorHandler collectorHandler) {
        this.kafkaConfig = kafkaConfig;
        this.consumer = new KafkaConsumer<>(kafkaConfig.getConsumerProperties());
        this.collectorHandler = collectorHandler;
    }

    @Override
    public void run() {
        try (consumer) {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(List.of(kafkaConfig.getTopics().get("hubs-events")));
            while (true) {
                var records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, HubEventAvro> rec : records) {
                    var event = rec.value();
                    collectorHandler.handle(event);
                    log.info("Received hub event: {}", event);
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
            log.error("Exception caught in run(): {}", e.getMessage(), e);
        }
    }
}