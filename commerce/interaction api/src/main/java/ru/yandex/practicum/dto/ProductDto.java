package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

@Builder
public record ProductDto(
        @NotBlank
        String productId,
        @NotBlank
        String productName,
        @NotBlank
        String description,
        @NotBlank
        String imageSrc,
        @NotBlank
        QuantityState quantityState,
        @NotBlank
        ProductState productState,
        double rating,
        @NotBlank
        ProductCategory productCategory,
        @Min(1)
        float price) {
}
