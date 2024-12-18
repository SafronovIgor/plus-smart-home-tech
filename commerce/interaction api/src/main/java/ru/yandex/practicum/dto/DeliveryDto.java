package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Builder
public record DeliveryDto(
        @NotNull UUID orderId,
        AddressDto toAddress,
        AddressDto fromAddress,
        DeliveryState deliveryState) {
}