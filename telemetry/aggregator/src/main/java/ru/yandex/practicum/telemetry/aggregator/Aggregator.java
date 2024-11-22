package ru.yandex.practicum.telemetry.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Aggregator {
    public static void main(String[] args) {
        var context = SpringApplication.run(Aggregator.class, args);
        var aggregationStarter = context.getBean(AggregatorStarter.class);
        aggregationStarter.start();
    }
}