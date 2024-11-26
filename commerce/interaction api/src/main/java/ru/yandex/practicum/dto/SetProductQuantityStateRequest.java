package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import ru.yandex.practicum.enums.QuantityState;

public record SetProductQuantityStateRequest(
        @NotBlank
        String productId,
        @NotBlank
        QuantityState quantityState) {
}