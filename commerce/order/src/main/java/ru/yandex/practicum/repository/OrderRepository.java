package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByUsername(String username);
}