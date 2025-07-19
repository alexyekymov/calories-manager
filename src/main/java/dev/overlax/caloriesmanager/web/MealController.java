package dev.overlax.caloriesmanager.web;

import dev.overlax.caloriesmanager.service.MealService;
import dev.overlax.caloriesmanager.web.mapper.MealResponseMapper;
import dev.overlax.caloriesmanager.web.payload.MealResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<MealResponse> getAll() {
        return responseMapper.toDTOs(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponse> get(@PathVariable int id) {
        MealResponse resp = responseMapper.toDTO(service.getWithId(id));
        return ResponseEntity.ok(resp);
    }
}
