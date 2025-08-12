package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.MealRepository;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.util.UserUtils;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository repository;
    private final MealProcessor processor;
    private final UserService userService;

    public List<Meal> getAll() {
        return repository.findAll();
    }

    public Meal getWithId(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Meal with {} not found", id)));
    }

    public List<MealPayload> getBetween(LocalDate startDate, LocalTime startTime,
                                        LocalDate endDate, LocalTime endTime) {

        List<Meal> mealsByDateOrAll = repository.findAllByDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTime(
                DateTimeUtils.atStartOfDayOrMinDate(startDate), DateTimeUtils.atStartOfNextDayOrMaxDate(endDate));

        return processor.getFilteredResponse(mealsByDateOrAll, UserUtils.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    @Transactional
    public Meal save(Meal meal) {
        User user = userService.getById(UserUtils.TEST_USER_ID);
        meal.setUser(user);
        return repository.save(meal);
    }
}
