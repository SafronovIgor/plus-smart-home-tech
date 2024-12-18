package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto create(DeliveryDto deliveryDto);

    void setSuccessfulToDelivery(UUID orderId);

    void setPickedToDelivery(UUID orderId);

    void setFailedToDelivery(UUID orderId);

    float calculateDeliveryCost(OrderDto orderDto);
}