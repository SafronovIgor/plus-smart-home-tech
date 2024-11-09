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
@IdClass(ScenarioActionsId.class)
@Table(name = "scenario_actions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAction {

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
    @JoinColumn(name = "action_id", referencedColumnName = "id")
    Action action;
}