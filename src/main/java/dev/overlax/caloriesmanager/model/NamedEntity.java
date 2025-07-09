package dev.overlax.caloriesmanager.model;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name")
    protected String name;

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
