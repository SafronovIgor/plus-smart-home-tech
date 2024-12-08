package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseProduct {

    @Id
    @UuidGenerator
    @Column(name = "warehouse_product_id")
    UUID warehouseProductId;

    @Column(name = "fragile")
    boolean fragile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dimension_id", referencedColumnName = "dimension_id")
    Dimension dimension;

    @Column(name = "weight")
    double weight;

    @Column(name = "product_id")
    UUID productId;

    @Column(name = "quantity")
    long quantity;
}