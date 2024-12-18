package ru.yandex.practicum.dto;

import java.util.Map;
import java.util.UUID;

public record ProductReturnRequest(
        UUID orderId,
        Map<UUID, Long> products) {
}