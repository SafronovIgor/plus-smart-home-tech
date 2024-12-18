package ru.yandex.practicum.dto;

import lombok.Builder;
import ru.yandex.practicum.enums.OrderState;

import java.util.Map;
import java.util.UUID;

@Builder
public record OrderDto(
        UUID orderId,
        UUID deliveryId,
        Boolean fragile,
        Float totalPrice,
        OrderState state,
        Float productPrice,
        Float deliveryPrice,
        UUID shoppingCartId,
        Double deliveryWeight,
        Double deliveryVolume,
        Map<UUID, Long> products) {
}