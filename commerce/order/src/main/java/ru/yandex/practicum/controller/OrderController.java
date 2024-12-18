package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public OrderDto get(@NotBlank @RequestParam String username) {
        log.info("Get and return Order by username {}", username);
        return orderService.get(username);
    }

    @PutMapping
    public OrderDto create(@Valid @RequestBody CreateNewOrderRequest createNewOrderRequest) {
        log.info("Creating and returning new order {}", createNewOrderRequest);
        return orderService.create(createNewOrderRequest);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@Valid ProductReturnRequest productReturnRequest) {
        log.info("Returning and saving order {}", productReturnRequest);
        return orderService.returnOrder(productReturnRequest);
    }

    @PostMapping("/payment")
    public OrderDto pay(@NotNull @RequestBody UUID orderId) {
        log.info("Paying and saving order with id {}", orderId);
        return orderService.pay(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto abortOrderByPaymentFailed(@NotNull @RequestBody UUID orderId) {
        log.info("Aborting order with id {} due to payment failure", orderId);
        return orderService.abortByPayment(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto deliver(@NotNull @RequestBody UUID orderId) {
        log.info("Delivering and saving order with id {}", orderId);
        return orderService.deliver(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto abortDeliverByFail(@NotNull @RequestBody UUID orderId) {
        log.info("Aborting delivery for order with id {}", orderId);
        return orderService.abortByFail(orderId);
    }

    @PostMapping("/completed")
    public OrderDto complete(@NotNull @RequestBody UUID orderId) {
        log.info("Completing and saving order with id {}", orderId);
        return orderService.complete(orderId);
    }

    @PostMapping("/total")
    public OrderDto calculateOrderCost(@NotNull @RequestBody UUID orderId) {
        log.info("Calculating total cost for order with id {}", orderId);
        return orderService.calculateOrderCost(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@NotNull @RequestBody UUID orderId) {
        log.info("Calculating delivery cost for order with id {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@NotNull @RequestBody UUID orderId) {
        log.info("Assembling and saving order with id {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto abortAssemblyByFail(@NotNull @RequestBody UUID orderId) {
        log.info("Aborting assembly for order with id {}", orderId);
        return orderService.abortAssemblyByFail(orderId);
    }
}