package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto create(@RequestBody final OrderDto order);

    @PostMapping("/refund")
    ResponseEntity<Void> refund(UUID orderId);

    @PostMapping("/failed")
    void paymentFailed(UUID orderId);

    @PostMapping("/totalCost")
    float calculateTotalCost(@RequestBody final OrderDto order);

    @PostMapping("/productCost")
    float calculateProductCost(@RequestBody final OrderDto order);
}