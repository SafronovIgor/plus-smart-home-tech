package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.entity.Order;

public interface OrderMapper {
    OrderDto toOrderDto(Order order);
}