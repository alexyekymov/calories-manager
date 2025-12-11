package dev.overlax.caloriesmanager.web.mapper;

import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPayloadMapper extends Mappable<User, UserPayload> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateFromDTO(UserPayload userReq, @MappingTarget User user);

    @AfterMapping
    default void fixNulls(@MappingTarget User user) {
        if (user.getMeals() == null) user.setMeals(Collections.emptyList());
        if (user.getRegistered() == null) user.setRegistered(new Date());
    }
}
