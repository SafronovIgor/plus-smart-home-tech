package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.entity.Address;

public interface AddressMapper {
    Address toAddress(AddressDto addressDto);
}