package com.furnistyle.furniturebackend.mappers;

import java.util.List;

public interface EntityMapper<E, D> {
    D toDTO(E e);

    E toEntity(D d);

    List<D> toDTOs(List<E> e);

    List<E> toEntities(List<D> d);
}
