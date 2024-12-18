package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.ShoppingCartProduct;
import ru.yandex.practicum.entity.ShoppingCartProductId;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, ShoppingCartProductId> {

    List<ShoppingCartProduct> findAllByCartProductIdShoppingCartId(UUID shoppingCartId);
}