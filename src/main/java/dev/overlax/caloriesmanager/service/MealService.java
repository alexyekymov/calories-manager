package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.MealRepository;
import dev.overlax.caloriesmanager.util.DateTimeUtils;
import dev.overlax.caloriesmanager.util.exception.DataConflictException;
import dev.overlax.caloriesmanager.web.mapper.MealPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealService {

    private final MealRepository repository;
    private final MealProcessor processor;
    private final UserService userService;
    private final MealPayloadMapper mapper;
    private final MessageService messageService;

    public List<MealPayload> getAll(int userId, int caloriesPerDay) {
        List<Meal> mealsById = repository.findAllByUserId(userId);
        return processor.getPayloadList(mealsById, caloriesPerDay);
    }

    public Meal get(int mealId, int userId) {
        Optional<Meal> user = repository.findByIdAndUserId(mealId, userId);

        return user.orElseThrow(() -> {
            var errMsg = messageService.getMessage("error.meal.not-belonged", mealId, userId);
            return new DataConflictException(errMsg);
        });
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
                .orElseThrow(() -> {
                    var errMsg = messageService.getMessage("error.meal.not-belonged", id, userId);
                    return new DataConflictException(errMsg);
                });

        mapper.updateFromDTO(mealReq, mealToUpdate);
    }

    @Transactional
    public void delete(int id, int userId) {
        if (repository.deleteByIdAndUserId(id, userId) < 1) {
            var errMsg = messageService.getMessage("error.meal.not-belonged", id, userId);
            throw new DataConflictException(errMsg);
        }
    }
}
