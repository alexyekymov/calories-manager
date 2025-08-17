package dev.overlax.caloriesmanager.web.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public record UserPayload(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonView(Views.Public.class)
        Integer id,

        @JsonView(Views.Public.class)
        String name,

        @JsonView(Views.Public.class)
        String email,

        String password,

        @JsonView(Views.Public.class)
        Integer caloriesPerDay
) {
}
