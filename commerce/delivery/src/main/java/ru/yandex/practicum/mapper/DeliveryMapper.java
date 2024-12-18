package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.entity.Delivery;

public interface DeliveryMapper {

    Delivery toDelivery(DeliveryDto deliveryDto);

    DeliveryDto toDeliveryDto(Delivery delivery);
}