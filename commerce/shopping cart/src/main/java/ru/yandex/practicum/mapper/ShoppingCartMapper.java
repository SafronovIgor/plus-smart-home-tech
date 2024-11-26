package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.entity.ShoppingCartProduct;

import java.util.List;

public interface ShoppingCartMapper {

    ShoppingCart toShoppingCart(ShoppingCartDto shoppingCartDto, String username);

    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart, List<ShoppingCartProduct> products);
}
