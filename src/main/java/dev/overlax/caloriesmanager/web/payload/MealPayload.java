package dev.overlax.caloriesmanager.web.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Schema()
public record MealPayload(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonView(Views.Public.class)
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Integer id,

        @JsonView(Views.Public.class)
        @NotNull
        LocalDateTime dateTime,

        @JsonView(Views.Public.class)
        @NotBlank
        @Size(min = 2, max = 120)
        String description,

        @JsonView(Views.Public.class)
        @Range(min = 10, max = 5000)
        Integer calories,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        boolean excess
) {
}
