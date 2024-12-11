package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;

public record AddProductToWarehouseRequest(@NotBlank String productId, long quantity) {
}