package ru.yandex.practicum.telemetry.configuration;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {
    Map<String, String> topics;
    Properties consumerProperties;
}