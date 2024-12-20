package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

public interface ShoppingCartService {

    ShoppingCartDto get(String username);

    ShoppingCartDto addProducts(String username, Map<String, Long> products);

    void deactivate(String username);

    ShoppingCartDto update(String username, Map<String, Long> products);

    ShoppingCartDto changeProductQuantity(String username,
                                          ChangeProductQuantityRequest changeProductQuantityRequest);

    BookedProductsDto book(String username);

    ShoppingCartDto getById(String username);
}