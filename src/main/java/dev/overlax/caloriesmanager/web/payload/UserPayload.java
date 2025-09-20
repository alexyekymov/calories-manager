package dev.overlax.caloriesmanager.web.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserPayload(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonView(Views.Public.class)
        Integer id,

        @Size(min = 2, max = 64)
        @JsonView(Views.Public.class)
        String name,

        @JsonView(Views.Public.class)
        @Email
        String email,

        @Size(min = 5, max = 32)
        String password,

        @JsonView(Views.Public.class)
        @Min(value = 750)
        @Max(value = 10000)
        Integer caloriesPerDay,

        @JsonView(Views.WithChildren.class)
        List<MealPayload> meals
) {
}
