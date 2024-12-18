package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUsernameIgnoreCaseAndActivated(String username, boolean isActivated);

    boolean existsByUsernameIgnoreCaseAndActivated(String username, boolean activated);
}