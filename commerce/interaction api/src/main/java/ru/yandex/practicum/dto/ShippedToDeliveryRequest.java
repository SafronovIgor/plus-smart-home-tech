package ru.yandex.practicum.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ShippedToDeliveryRequest(
        UUID orderId,
        UUID deliveryId) {
}