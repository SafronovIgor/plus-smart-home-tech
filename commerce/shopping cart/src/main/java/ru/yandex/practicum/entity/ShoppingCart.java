package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_carts")
@EqualsAndHashCode(of = "shoppingCartId")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {

    @Id
    @UuidGenerator
    @Column(name = "shopping_cart_id", nullable = false)
    UUID shoppingCartId;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "activated", nullable = false)
    boolean activated;
}