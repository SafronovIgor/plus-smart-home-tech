package ru.yandex.practicum.telemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.yandex.practicum.telemetry.processor.hub.event.HubEventProcessor;
import ru.yandex.practicum.telemetry.processor.snapshot.SnapshotProcessor;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Analyzer {
    public static void main(String[] args) {
        var context = SpringApplication.run(Analyzer.class, args);

        final var hubEventProcessor = context.getBean(HubEventProcessor.class);
        final var snapshotProcessor = context.getBean(SnapshotProcessor.class);

        // запускаем в отдельном потоке обработчик событий
        // от пользовательских хабов
        var hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        // В текущем потоке начинаем обработку
        // снимков состояния датчиков
        snapshotProcessor.start();
    }
}