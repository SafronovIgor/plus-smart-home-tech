package ru.yandex.practicum.telemetry.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conditions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "type")
    ConditionType type;

    @Column(name = "operation")
    ConditionOperation operation;

    @Column(name = "value")
    Integer value;
}