package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {

    OrderDto get(String username);

    OrderDto create(CreateNewOrderRequest createNewOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto pay(UUID orderId);

    OrderDto abortByPayment(UUID orderId);

    OrderDto deliver(UUID orderId);

    OrderDto abortByFail(UUID orderId);

    OrderDto complete(UUID orderId);

    OrderDto calculateOrderCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);

    OrderDto assembly(UUID orderId);

    OrderDto abortAssemblyByFail(UUID orderId);
}