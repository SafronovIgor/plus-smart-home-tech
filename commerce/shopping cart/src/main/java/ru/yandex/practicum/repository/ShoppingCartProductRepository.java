package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.entity.ShoppingCartProduct;
import ru.yandex.practicum.entity.ShoppingCartProductId;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, ShoppingCartProductId> {

    List<ShoppingCartProduct> findAllByCartProductId_ShoppingCartId(UUID shoppingCartId);
}