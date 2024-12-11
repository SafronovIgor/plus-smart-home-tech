package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shopping_carts")
@EqualsAndHashCode(of = "shoppingCartId")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {

    @Id
    @UuidGenerator
    @Column(name = "shopping_cart_id")
    UUID shoppingCartId;

    @NotBlank
    @Column(name = "username")
    String username;

    @Column(name = "activated")
    boolean activated;
}