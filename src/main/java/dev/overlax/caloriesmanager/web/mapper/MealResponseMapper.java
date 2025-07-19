package dev.overlax.caloriesmanager.web.mapper;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.web.payload.MealResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealResponseMapper extends Mappable<Meal, MealResponse> {
}
