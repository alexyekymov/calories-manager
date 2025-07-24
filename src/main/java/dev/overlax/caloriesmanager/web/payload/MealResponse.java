package dev.overlax.caloriesmanager.web.payload;

import java.time.LocalDateTime;

public record MealResponse(
        LocalDateTime dateTime,
        String description,
        Integer calories,
        boolean excess
) {
}
