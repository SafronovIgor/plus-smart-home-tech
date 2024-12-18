package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.entity.Payment;

public interface PaymentMapper {
    PaymentDto toPaymentDto(Payment payment);
}
