package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository repository;

    public List<Meal> getAll() {
        return repository.findAll();
    }
}
