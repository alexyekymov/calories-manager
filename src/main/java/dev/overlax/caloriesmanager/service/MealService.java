package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.MealRepository;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.web.mapper.MealPayloadMapper;
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
    private final MealPayloadMapper mapper;

    public List<MealPayload> getAll(int userId, int caloriesPerDay) {
        List<Meal> mealsById = repository.findAllByUserId(userId);
        return processor.getPayloadList(mealsById, caloriesPerDay);

    }

    public Meal get(int mealId, int userId) {
        return repository.findByIdAndUserId(mealId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Meal with %d not found", mealId)));
    }

    public List<MealPayload> getBetween(int userId, int caloriesPerDay,
                                        LocalDate startDate, LocalTime startTime,
                                        LocalDate endDate, LocalTime endTime) {

        List<Meal> mealsByDateOrAll = repository.findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTime(
                userId,
                DateTimeUtils.atStartOfDayOrMinDate(startDate),
                DateTimeUtils.atStartOfNextDayOrMaxDate(endDate));

        return processor.getFilteredPayloadList(mealsByDateOrAll, caloriesPerDay, startTime, endTime);
    }

    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = userService.getById(userId);
        meal.setUser(user);
        return repository.save(meal);
    }

    @Transactional
    public void update(MealPayload mealReq, int id, int userId) {

        Meal mealToUpdate = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("The meal with id=" + id +
                        " doesn't belong to the user with id=" + userId));

        mapper.updateFromDTO(mealReq, mealToUpdate);
    }

    @Transactional
    public void delete(int id, int userId) {
        if (repository.deleteByIdAndUserId(id, userId) < 1) {
            throw new RuntimeException("The meal with id=" + id +
                    " doesn't belong to the user with id=" + userId);
        }
    }
}
