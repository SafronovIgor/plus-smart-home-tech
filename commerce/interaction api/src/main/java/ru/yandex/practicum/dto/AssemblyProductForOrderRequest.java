package ru.yandex.practicum.dto;

import lombok.Builder;

import java.util.Map;
import java.util.UUID;

@Builder
public record AssemblyProductForOrderRequest(
        Map<UUID, Long> products,
        UUID orderId) {
}