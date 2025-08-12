package dev.overlax.caloriesmanager.util;

import dev.overlax.caloriesmanager.model.HasId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new RuntimeException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}