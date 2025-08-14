package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.web.mapper.MealPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MealProcessor {

    private final MealPayloadMapper mapper;

    public List<MealPayload> getPayloadList(List<Meal> meals, int caloriesLimit) {
        return filterByPredicate(meals, caloriesLimit, meal -> true);
    }

    public List<MealPayload> getFilteredPayloadList(List<Meal> meals, int caloriesLimit,
                                                    LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesLimit,
                meal -> DateTimeUtils.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    private List<MealPayload> filterByPredicate(List<Meal> meals, int caloriesLimit,
                                                Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDade = meals.stream()
                .collect(
                        Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> mapper.toDTOWithExcess(meal, caloriesSumByDade.get(meal.getDate()) > caloriesLimit))
                .toList();
    }
}
