package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto create(DeliveryDto deliveryDto) {
        DeliveryDto savedDeliveryDto = deliveryService.create(deliveryDto);
        log.info("Delivery created: input={}, result={}", deliveryDto, savedDeliveryDto);
        return savedDeliveryDto;
    }

    @PostMapping("/successful")
    public void setDeliveryStatusSuccessful(UUID orderId) {
        deliveryService.setSuccessfulToDelivery(orderId);
        log.info("Delivery status update: action=setSuccessful, orderId={}", orderId);
    }

    @PostMapping("/picked")
    public void setDeliveryStatusPicked(UUID orderId) {
        deliveryService.setPickedToDelivery(orderId);
        log.info("Delivery status update: action=setPicked, orderId={}", orderId);
    }

    @PostMapping("/failed")
    public void setFailedStatusToDelivery(UUID orderId) {
        deliveryService.setFailedToDelivery(orderId);
        log.info("Delivery status update: action=setFailed, orderId={}", orderId);
    }

    @PostMapping("/cost")
    public Float calculateDeliveryCost(OrderDto orderDto) {
        Float calculatedDeliveryCost = deliveryService.calculateDeliveryCost(orderDto);
        log.info("Delivery cost calculation: order={}, cost={}", orderDto, calculatedDeliveryCost);
        return calculatedDeliveryCost;
    }
}