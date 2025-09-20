package dev.overlax.caloriesmanager.web.payload;

public record ApiSubError(
        String object,
        String field,
        String rejectedValue,
        String message
) {
}
