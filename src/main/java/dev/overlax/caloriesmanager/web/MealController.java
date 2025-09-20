package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.AuthUser;
import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.service.MealService;
import dev.overlax.caloriesmanager.web.mapper.MealPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import dev.overlax.caloriesmanager.web.payload.Views;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MealPayloadMapper payloadMapper;

    @GetMapping
    public ResponseEntity<List<MealPayload>> getAll(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(service.getAll(authUser.id(), authUser.getUser().getCaloriesPerDay()));
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class) // to show only fields without calculations of excess
    public ResponseEntity<MealPayload> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        MealPayload resp = payloadMapper.toDTO(service.get(id, authUser.id()));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MealPayload>> getBetween(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        List<MealPayload> mealsBetween = service.getBetween(authUser.id(), authUser.getUser().getCaloriesPerDay(),
                startDate, startTime, endDate, endTime);
        return ResponseEntity.ok(mealsBetween);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class) // to show only fields without calculations of excess
    public ResponseEntity<MealPayload> createWithLocation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid MealPayload mealReq,
            UriComponentsBuilder uriBuilder) {
        Meal meal = payloadMapper.toEntity(mealReq);
        Meal created = service.save(meal, authUser.id());
        return ResponseEntity
                .created(uriBuilder
                        .path(REST_URI + "/{id}")
                        .build(created.getId()))
                .body(payloadMapper.toDTO(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable int id,
            @RequestBody @Valid MealPayload mealReq) {

        service.update(mealReq, id, authUser.id());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        service.delete(id, authUser.id());
        return ResponseEntity.noContent().build();
    }
}
