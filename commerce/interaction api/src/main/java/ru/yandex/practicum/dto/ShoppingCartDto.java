package ru.yandex.practicum.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ShoppingCartDto(
        String shoppingCartId,
        Map<String, Long> products) {
}