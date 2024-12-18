package ru.yandex.practicum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "addressId")
@Table(name = "addresses")
public class Address {

    @Id
    UUID addressId;

    String country;

    String city;

    String street;

    String house;

    String flat;
}