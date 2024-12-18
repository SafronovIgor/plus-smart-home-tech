package ru.yandex.practicum.mapper;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.entity.Order;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }
}