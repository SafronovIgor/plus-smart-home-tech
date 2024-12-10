package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record BookedProductsDto(@Positive double deliveryWeight, @Positive double deliveryVolume, boolean fragile) {
}