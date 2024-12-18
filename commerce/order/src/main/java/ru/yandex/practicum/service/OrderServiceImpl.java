package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.entity.Order;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.AddressMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    public OrderDto get(String username) {
        Order order = orderRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Order with username: " + username + " not found"));
        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto create(CreateNewOrderRequest request) {
        BookedProductsDto bookedProducts = warehouseClient.checkForProductsSufficiency(request.shoppingCart());
        Order order = buildNewOrder(request, bookedProducts);
        orderRepository.save(order);

        DeliveryDto delivery = createDelivery(order, request.deliveryAddress());
        order.setDeliveryId(delivery.orderId());

        PaymentDto payment = paymentClient.create(orderMapper.toOrderDto(order));
        updateOrderWithPaymentInfo(order, payment);

        return saveAndReturnDto(order);
    }

    @Override
    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = getOrderOrThrow(request.orderId());
        warehouseClient.returnProducts(request.products());
        return updateOrderState(order, OrderState.PRODUCT_RETURNED);
    }

    @Override
    public OrderDto pay(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        paymentClient.refund(orderId);
        return updateOrderState(order, OrderState.PAID);
    }

    @Override
    public OrderDto abortByPayment(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        paymentClient.paymentFailed(orderId);
        return updateOrderState(order, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        try {
            warehouseClient.assemblyProductsForOrder(
                    new AssemblyProductForOrderRequest(order.getProducts(), orderId));
            return updateOrderState(order, OrderState.ASSEMBLED);
        } catch (ProductNotFoundException | ProductInShoppingCartLowQuantityInWarehouse e) {
            return abortAssemblyByFail(orderId);
        }
    }

    @Override
    public OrderDto abortAssemblyByFail(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return updateOrderState(order, OrderState.ASSEMBLED_FAILED);
    }

    @Override
    public OrderDto deliver(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        deliveryClient.setDeliveryStatusSuccessful(orderId);
        return updateOrderState(order, OrderState.DELIVERED);
    }

    @Override
    public OrderDto abortByFail(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        deliveryClient.setFailedStatusToDelivery(orderId);
        return updateOrderState(order, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        return updateOrderState(order, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateOrderCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getTotalPrice() == null) {
            order.setTotalPrice(paymentClient.calculateTotalCost(orderMapper.toOrderDto(order)));
            orderRepository.save(order);
        }
        return orderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getDeliveryPrice() == null) {
            order.setDeliveryPrice(deliveryClient.calculateDeliveryCost(orderMapper.toOrderDto(order)));
            orderRepository.save(order);
        }
        return orderMapper.toOrderDto(order);
    }

    private Order getOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Order with id " + orderId + " not found"));
    }

    private OrderDto updateOrderState(Order order, OrderState state) {
        order.setState(state);
        return saveAndReturnDto(order);
    }

    private Order buildNewOrder(CreateNewOrderRequest request, BookedProductsDto bookedProducts) {
        return Order.builder()
                .shoppingCartId(request.shoppingCart().shoppingCartId())
                .state(OrderState.NEW)
                .products(request.shoppingCart().products())
                .deliveryVolume(bookedProducts.deliveryVolume())
                .deliveryWeight(bookedProducts.deliveryWeight())
                .fragile(bookedProducts.fragile())
                .address(addressMapper.toAddress(request.deliveryAddress()))
                .build();
    }

    private DeliveryDto createDelivery(Order order, AddressDto deliveryAddress) {
        return deliveryClient.create(DeliveryDto.builder()
                .fromAddress(warehouseClient.getWarehouseAddress())
                .toAddress(deliveryAddress)
                .orderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build());
    }

    private void updateOrderWithPaymentInfo(Order order, PaymentDto payment) {
        order.setPaymentId(payment.paymentId());
        order.setTotalPrice(payment.totalPayment());
        order.setDeliveryPrice(payment.deliveryTotal());
        order.setProductPrice(paymentClient.calculateProductCost(orderMapper.toOrderDto(order)));
    }

    private OrderDto saveAndReturnDto(Order order) {
        return orderMapper.toOrderDto(orderRepository.save(order));
    }
}