package ru.yandex.practicum.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartProductId implements Serializable {

    UUID shoppingCartId;

    UUID productId;
}