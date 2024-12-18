package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AddressDto(@NotBlank String country,
                         @NotBlank String city,
                         @NotBlank String street,
                         @NotBlank String house,
                         @NotBlank String flat) {
}