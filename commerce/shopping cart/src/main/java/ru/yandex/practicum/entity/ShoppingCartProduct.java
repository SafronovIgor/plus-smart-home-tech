package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "carts_products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartProduct {

    @EmbeddedId
    ShoppingCartProductId cartProductId;

    @Column(name = "quantity")
    long quantity;
}