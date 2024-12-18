package ru.yandex.practicum.dto;

import lombok.Builder;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Builder
public record DeliveryDto(
        UUID orderId,
        AddressDto toAddress,
        AddressDto fromAddress,
        DeliveryState deliveryState) {
}