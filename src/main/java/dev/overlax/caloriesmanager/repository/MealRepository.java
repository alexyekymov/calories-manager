package dev.overlax.caloriesmanager.repository;

import dev.overlax.caloriesmanager.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    List<Meal> findAllByDateTimeBetween(LocalDateTime dateTimeAfter, LocalDateTime dateTimeBefore);

    List<Meal> findAllByDateTimeAfterAndDateTimeBefore(LocalDateTime dateTimeAfter, LocalDateTime dateTimeBefore);
}
