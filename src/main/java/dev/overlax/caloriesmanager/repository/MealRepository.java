package dev.overlax.caloriesmanager.repository;

import dev.overlax.caloriesmanager.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    List<Meal> findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTime(
            int userId,
            LocalDateTime dateTimeAfter,
            LocalDateTime dateTimeBefore);

    Optional<Meal> findByIdAndUserId(int mealId, int userId);

    int deleteByIdAndUserId(int mealId, int userId);

    List<Meal> findAllByUserId(int userId);
}
