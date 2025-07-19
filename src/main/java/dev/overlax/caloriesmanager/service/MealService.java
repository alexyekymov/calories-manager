package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository repository;

    public List<Meal> getAll() {
        return repository.findAll();
    }

    public Meal getWithId(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Meal with {} not found", id)));
    }

    public List<Meal> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        //TODO fix a null-value issue
        return repository.
                findAllByDateTimeBetween(LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime));
    }
}
