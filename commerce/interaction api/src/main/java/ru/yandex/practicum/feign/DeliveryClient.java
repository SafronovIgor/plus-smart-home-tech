package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient {

    @PutMapping
    DeliveryDto create(DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void setDeliveryStatusSuccessful(UUID orderId);

    @PostMapping("/picked")
    void setDeliveryStatusPicked(UUID orderId);

    @PostMapping("/failed")
    void setFailedStatusToDelivery(UUID orderId);

    @GetMapping("/cost")
    Float calculateDeliveryCost(OrderDto orderDto);
}