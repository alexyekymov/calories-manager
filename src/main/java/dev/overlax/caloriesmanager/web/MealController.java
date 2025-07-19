package dev.overlax.caloriesmanager.web;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.service.MealService;
import dev.overlax.caloriesmanager.web.mapper.MealResponseMapper;
import dev.overlax.caloriesmanager.web.payload.MealResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/profile/meals", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class MealController {

    private final MealService service;
    private final MealResponseMapper responseMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MealResponse>> getAll() {
        List<MealResponse> resp = responseMapper.toDTOs(service.getAll());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> get(@PathVariable int id) {
        MealResponse resp = responseMapper.toDTO(service.getWithId(id));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MealResponse>> getBetween(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        List<Meal> mealsBetween = service.getBetween(startDate, startTime, endDate, endTime);
        return ResponseEntity.ok(responseMapper.toDTOs(mealsBetween));
    }
}
