package dev.overlax.caloriesmanager.repository;

import dev.overlax.caloriesmanager.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {
}
