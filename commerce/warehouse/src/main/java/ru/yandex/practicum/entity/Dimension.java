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
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dimensions")
@EqualsAndHashCode(of = "dimensionId")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dimension {

    @Id
    @UuidGenerator
    @Column(name = "dimension_id")
    UUID dimensionId;

    @Column(name = "width")
    double width;

    @Column(name = "height")
    double height;

    @Column(name = "depth")
    double depth;
}