package ru.yandex.practicum.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("warehouse.address")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseAddress {
    String country;
    String city;
    String street;
    String house;
    String flat;
}