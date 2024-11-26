package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.entity.ShoppingCartProduct;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingCartMapperImpl implements ShoppingCartMapper {

    @Override
    public ShoppingCart toShoppingCart(ShoppingCartDto shoppingCartDto, String username) {
        return ShoppingCart.builder()
                .shoppingCartId(UUID.fromString(shoppingCartDto.shoppingCartId()))
                .username(username)
                .build();
    }

    @Override
    public ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart, List<ShoppingCartProduct> products) {
        Map<String, Long> productsMap = products.stream()
                .collect(Collectors.toMap(cartProduct ->
                        cartProduct.getCartProductId().getProductId().toString(), ShoppingCartProduct::getQuantity)
                );

        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId().toString())
                .products(productsMap)
                .build();
    }
}