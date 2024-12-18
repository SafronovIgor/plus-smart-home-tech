package ru.yandex.practicum.dto;

import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Builder
public record ShoppingCartDto(
        Map<UUID, Long> products,
        UUID shoppingCartId) {
}
