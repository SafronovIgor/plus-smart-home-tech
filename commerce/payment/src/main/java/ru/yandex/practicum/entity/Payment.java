package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.enums.PaymentState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@EqualsAndHashCode(of = "paymentId")
public class Payment {

    @Id
    @UuidGenerator
    UUID paymentId;

    @Column(name = "order_id")
    UUID orderId;

    @Column(name = "total_payment")
    Float totalPayment;

    @Column(name = "delivery_total")
    Float deliveryTotal;

    @Column(name = "fee_total")
    Float feeTotal;

    @Column(name = "payment_state")
    PaymentState paymentState;
}