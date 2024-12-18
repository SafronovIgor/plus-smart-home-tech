package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public OrderDto get(@RequestParam String username) {
        log.info("Get and return Order by username {}", username);
        return orderService.get(username);
    }

    @PutMapping
    public OrderDto create(@RequestBody CreateNewOrderRequest createNewOrderRequest) {
        log.info("Creating and returning new order {}", createNewOrderRequest);
        return orderService.create(createNewOrderRequest);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(ProductReturnRequest productReturnRequest) {
        log.info("Returning and saving order {}", productReturnRequest);
        return orderService.returnOrder(productReturnRequest);
    }

    @PostMapping("/payment")
    public OrderDto pay(@RequestBody UUID orderId) {
        log.info("Paying and saving order with id {}", orderId);
        return orderService.pay(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto abortOrderByPaymentFailed(@RequestBody UUID orderId) {
        log.info("Aborting order with id {} due to payment failure", orderId);
        return orderService.abortByPayment(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliver(@RequestBody UUID orderId) {
        log.info("Delivering and saving order with id {}", orderId);
        return orderService.deliver(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto abortDeliverByFail(@RequestBody UUID orderId) {
        log.info("Aborting delivery for order with id {}", orderId);
        return orderService.abortByFail(orderId);
    }

    @PostMapping("/completed")
    public OrderDto complete(@RequestBody UUID orderId) {
        log.info("Completing and saving order with id {}", orderId);
        return orderService.complete(orderId);
    }

    @PostMapping("/total")
    public OrderDto calculateOrderCost(@RequestBody UUID orderId) {
        log.info("Calculating total cost for order with id {}", orderId);
        return orderService.calculateOrderCost(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        log.info("Calculating delivery cost for order with id {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody UUID orderId) {
        log.info("Assembling and saving order with id {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto abortAssemblyByFail(@RequestBody UUID orderId) {
        log.info("Aborting assembly for order with id {}", orderId);
        return orderService.abortAssemblyByFail(orderId);
    }
}