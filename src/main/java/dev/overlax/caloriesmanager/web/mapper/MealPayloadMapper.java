package dev.overlax.caloriesmanager.web.mapper;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealPayloadMapper extends Mappable<Meal, MealPayload> {
    MealPayload toDTOWithExcess(Meal meal, boolean excess);
}
