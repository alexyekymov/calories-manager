package dev.overlax.caloriesmanager.model;

public interface HasId {

    Integer getId();

    void setId();

    default boolean isNew() {
        return getId() == null;
    }
}
