package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto create(@RequestBody final OrderDto order) {
        log.info("Creating payment for order: {}", order);
        return paymentService.create(order);
    }

    @PostMapping("/totalCost")
    public float calculateTotalCost(@RequestBody final OrderDto order) {
        log.info("Calculating total cost for order: {}", order);
        return paymentService.calculateTotalCost(order);
    }

    @PostMapping("/refund")
    public ResponseEntity<Void> refund(UUID orderId) {
        log.info("Processing refund for order: {}", orderId);
        return paymentService.refund(orderId);
    }

    @PostMapping("/productCost")
    public float calculateProductCost(@RequestBody final OrderDto order) {
        log.info("Calculating product cost for order: {}", order);
        return paymentService.calculateProductCost(order);
    }

    @PostMapping("/failed")
    public void paymentFailed(UUID orderId) {
        log.info("Processing failed payment for order: {}", orderId);
        paymentService.paymentFailed(orderId);
    }
}