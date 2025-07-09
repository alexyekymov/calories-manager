package dev.overlax.caloriesmanager.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    protected Integer id;

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getId();
    }
}
