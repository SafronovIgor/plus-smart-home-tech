package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.entity.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}