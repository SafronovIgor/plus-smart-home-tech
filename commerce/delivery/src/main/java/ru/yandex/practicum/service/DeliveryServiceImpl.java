package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.entity.Address;
import ru.yandex.practicum.entity.Delivery;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Value("${delivery.baseCost}")
    private float baseCost;

    @Override
    @Transactional
    public DeliveryDto create(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery created: id={}, orderId={}", savedDelivery.getDeliveryId(), savedDelivery.getOrderId());
        return deliveryMapper.toDeliveryDto(savedDelivery);
    }

    @Override
    @Transactional
    public void setSuccessfulToDelivery(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        updateDeliveryState(delivery, DeliveryState.DELIVERED);
        orderClient.deliver(orderId);
        log.info("Delivery marked as SUCCESSFUL: deliveryId={}, orderId={}", delivery.getDeliveryId(), orderId);
    }

    @Override
    @Transactional
    public void setPickedToDelivery(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        updateDeliveryState(delivery, DeliveryState.IN_PROGRESS);
        warehouseClient.shipToDelivery(new ShippedToDeliveryRequest(delivery.getOrderId(), delivery.getDeliveryId()));
        log.info("Delivery marked as PICKED: deliveryId={}, orderId={}", delivery.getDeliveryId(), orderId);
    }

    @Override
    @Transactional
    public void setFailedToDelivery(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        updateDeliveryState(delivery, DeliveryState.FAILED);
        orderClient.abortDeliverByFail(orderId);
        log.info("Delivery marked as FAILED: deliveryId={}, orderId={}", delivery.getDeliveryId(), orderId);
    }

    @Override
    public float calculateDeliveryCost(OrderDto orderDto) {
        Delivery delivery = findDeliveryByOrderId(orderDto.orderId());
        float costMultiplier = calculateCostMultiplier(delivery, orderDto);
        float deliveryCost = baseCost * costMultiplier;

        log.debug("Delivery cost calculated: baseCost={}, multiplier={}, finalCost={}",
                baseCost, costMultiplier, deliveryCost);

        return deliveryCost;
    }

    private Delivery findDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    private void updateDeliveryState(Delivery delivery, DeliveryState newState) {
        delivery.setDeliveryState(newState);
        deliveryRepository.save(delivery);
    }

    private float calculateCostMultiplier(Delivery delivery, OrderDto orderDto) {
        Address addressFrom = delivery.getFromAddress();
        Address addressTo = delivery.getToAddress();

        float multiplier = 1.0f;

        if (addressFrom.toString().contains("ADDRESS_2")) {
            multiplier *= 2.0f;
        }

        if (orderDto.fragile()) {
            multiplier *= 1.2f;
        }

        multiplier *= (float) (1 + (orderDto.deliveryWeight() * 0.3f));
        multiplier *= (float) (1 + (orderDto.deliveryVolume() * 0.2f));

        if (!addressTo.toString().contains("ADDRESS_1") && !addressTo.toString().contains("ADDRESS_2")) {
            multiplier *= 1.2f;
        }

        return multiplier;
    }
}