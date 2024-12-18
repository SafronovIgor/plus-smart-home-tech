package ru.yandex.practicum.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
@EqualsAndHashCode(of = "deliveryId")
public class Delivery {

    @Id
    @UuidGenerator
    private UUID deliveryId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "deliveryState")
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;
}