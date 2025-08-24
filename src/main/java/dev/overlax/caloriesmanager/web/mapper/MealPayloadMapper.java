package dev.overlax.caloriesmanager.web.mapper;

import dev.overlax.caloriesmanager.model.Meal;
import dev.overlax.caloriesmanager.web.payload.MealPayload;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MealPayloadMapper extends Mappable<Meal, MealPayload> {
    MealPayload toDTOWithExcess(Meal meal, boolean excess);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(MealPayload mealReq, @MappingTarget Meal meal);
}
