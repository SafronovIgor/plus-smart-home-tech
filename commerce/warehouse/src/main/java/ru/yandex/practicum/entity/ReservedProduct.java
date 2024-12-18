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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reserved_products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservedProduct {

    @Id
    @UuidGenerator
    @Column(name = "reserved_products_id")
    UUID reservedProductId;

    @Column(name = "shopping_cart_id")
    UUID shoppingCartId;

    @Column(name = "product_id")
    UUID productId;

    @Column(name = "reserved_quantity")
    long reservedQuantity;
}