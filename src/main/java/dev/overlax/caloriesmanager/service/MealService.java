package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.MealRepository;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.util.UserUtils;
import dev.overlax.caloriesmanager.util.ValidationUtil;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final MealRepository repository;
    private final MealProcessor processor;
    private final UserService userService;

    public List<MealPayload> getAll() {
        List<Meal> mealsById = repository.findAllByUserId(UserUtils.TEST_USER_ID);
        return processor.getPayloadList(mealsById, UserUtils.DEFAULT_CALORIES_PER_DAY);

    }

    public Meal get(int mealId, int userId) {
        return repository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Meal with %d not found", mealId)));
    }

    public List<MealPayload> getBetween(LocalDate startDate, LocalTime startTime,
                                        LocalDate endDate, LocalTime endTime) {

        List<Meal> mealsByDateOrAll = repository.findAllByDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTime(
                DateTimeUtils.atStartOfDayOrMinDate(startDate), DateTimeUtils.atStartOfNextDayOrMaxDate(endDate));

        return processor.getFilteredPayloadList(mealsByDateOrAll, UserUtils.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    @Transactional
    public Meal save(Meal meal) {
        User user = userService.getById(UserUtils.TEST_USER_ID);
        meal.setUser(user);
        return repository.save(meal);
    }

    @Transactional
    public void update(Meal newMeal, int id) {
        ValidationUtil.assureIdConsistent(newMeal, id);

        Meal mealToUpdate = repository.findByIdAndUserId(id, UserUtils.TEST_USER_ID)
                .orElseThrow(() -> new RuntimeException("The meal with id=" + id +
                        " doesn't belong to the user with id=" + UserUtils.TEST_USER_ID));

        mealToUpdate.setDateTime(newMeal.getDateTime());
        mealToUpdate.setDescription(newMeal.getDescription());
        mealToUpdate.setCalories(newMeal.getCalories());
    }

    @Transactional
    public void delete(int id) {
        if (repository.deleteByIdAndUserId(id, UserUtils.TEST_USER_ID) < 1) {
            throw new RuntimeException("The meal with id=" + id +
                    " doesn't belong to the user with id=" + UserUtils.TEST_USER_ID);
        }
    }
}
