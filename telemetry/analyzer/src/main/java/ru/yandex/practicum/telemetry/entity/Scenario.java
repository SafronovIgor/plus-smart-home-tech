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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "scenarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"hub_id", "name"})})
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "hub_id", nullable = false)
    String hubId;

    @Column(name = "name", nullable = false)
    String name;
}