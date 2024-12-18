package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record ProductReturnRequest(
        @NotNull UUID orderId,
        Map<UUID, Long> products) {
}