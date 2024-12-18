package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.OrderState;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "orders")
@EqualsAndHashCode(of = "orderId")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @UuidGenerator
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "username")
    private String username;

    @Column(name = "shopping_cart_id", nullable = false)
    private UUID shoppingCartId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @ElementCollection
    @CollectionTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "delivery_volume")
    private double deliveryVolume;

    @Column(name = "delivery_weight")
    private double deliveryWeight;

    @Column(name = "fragile")
    private boolean fragile;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "delivery_price")
    private Float deliveryPrice;

    @Column(name = "product_price")
    private Float productPrice;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
}