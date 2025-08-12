package dev.overlax.caloriesmanager.web.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;

public record MealPayload(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonView(Views.Public.class)
        Integer id,

        @JsonView(Views.Public.class)
        LocalDateTime dateTime,

        @JsonView(Views.Public.class)
        String description,

        @JsonView(Views.Public.class)
        Integer calories,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        boolean excess
) {
}
