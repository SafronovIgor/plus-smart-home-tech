package ru.yandex.practicum.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.entity.Delivery;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryMapperImpl implements DeliveryMapper {
    AddressMapper addressMapper;

    @Override
    public Delivery toDelivery(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .fromAddress(addressMapper.toAddress(deliveryDto.fromAddress()))
                .toAddress(addressMapper.toAddress(deliveryDto.toAddress()))
                .orderId(deliveryDto.orderId())
                .deliveryState(deliveryDto.deliveryState())
                .build();
    }

    @Override
    public DeliveryDto toDeliveryDto(Delivery delivery) {
        return DeliveryDto.builder()
                .fromAddress(addressMapper.toAddressDto(delivery.getFromAddress()))
                .toAddress(addressMapper.toAddressDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getDeliveryState())
                .build();
    }
}