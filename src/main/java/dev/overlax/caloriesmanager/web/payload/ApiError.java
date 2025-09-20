package dev.overlax.caloriesmanager.web.payload;

import java.time.Instant;
import java.util.List;

public record ApiError(
        String status,
        String error,
        String message,
        Instant timestamp,
        String path,
        List<ApiSubError> subErrors //TODO: ???
) {
}
