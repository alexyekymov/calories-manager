package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.web.mapper.MealResponseMapper;
import dev.overlax.caloriesmanager.web.payload.MealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MealProcessor {

    private final MealResponseMapper mapper;

    public List<MealResponse> getFilteredResponse(List<Meal> meals, int caloriesLimit,
                                                  LocalTime startTime, LocalTime endTime) {
        Map<LocalDate, Integer> caloriesSumByDade = meals.stream()
                .collect(
                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> DateTimeUtils.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> mapper.toDTOWithExcess(meal, caloriesSumByDade.get(meal.getDate()) > caloriesLimit))
                .toList();
    }
}
