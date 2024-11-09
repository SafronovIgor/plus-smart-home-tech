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
@IdClass(ScenarioConditionsId.class)
@Table(name = "scenario_conditions")
public class ScenarioCondition {

    @Id
    @ManyToOne
    @JoinColumn(name = "scenario_id", referencedColumnName = "id")
    Scenario scenario;

    @Id
    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    Sensor sensor;

    @Id
    @ManyToOne
    @JoinColumn(name = "condition_id", referencedColumnName = "id")
    Condition condition;
}