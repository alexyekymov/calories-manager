package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.service.MealService;
import dev.overlax.caloriesmanager.web.mapper.MealResponseMapper;
import dev.overlax.caloriesmanager.web.payload.MealResponse;
import dev.overlax.caloriesmanager.web.payload.Views;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealController.REST_URI, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class MealController {
    public static final String REST_URI = "/api/profile/meals";

    private final MealService service;
    private final MealResponseMapper responseMapper;

    @GetMapping
    public ResponseEntity<List<MealResponse>> getAll() {
        List<MealResponse> resp = responseMapper.toDTOs(service.getAll());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class) // to show only fields without calculations of excess
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

        List<MealResponse> mealsBetween = service.getBetween(startDate, startTime, endDate, endTime);
        return ResponseEntity.ok(mealsBetween);
    }

    @PostMapping
    @JsonView(Views.Public.class) // to show only fields without calculations of excess
    public ResponseEntity<MealResponse> createWithLocation(@RequestBody MealResponse mealReq, UriComponentsBuilder uriBuilder) {
        Meal meal = responseMapper.toEntity(mealReq);
        Meal created = service.save(meal);
        return ResponseEntity
                .created(uriBuilder
                        .path(REST_URI + "/{id}")
                        .build(created.getId()))
                .body(responseMapper.toDTO(created));
    }
}
