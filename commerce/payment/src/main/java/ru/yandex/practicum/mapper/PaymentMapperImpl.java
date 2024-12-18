package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.entity.Payment;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }
}