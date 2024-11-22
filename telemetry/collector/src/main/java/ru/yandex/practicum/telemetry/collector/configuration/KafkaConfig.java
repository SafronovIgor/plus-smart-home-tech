package ru.yandex.practicum.telemetry.collector.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("collector.kafka")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
    Map<String, String> topics;
    Properties producerProperties;
}
