package dev.overlax.caloriesmanager.web.mapper;


import java.util.List;

public interface Mappable<E, D> {

    D toDTO(E entity);

    List<D> toDTOs(List<E> entities);

    E toEntity(D dto);

    List<E> toEntities(List<D> dtoList);

}
