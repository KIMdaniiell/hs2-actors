package com.example.hs2actors.util;

public interface Mapper<T, E> {
    E entityToDto(T entity);
    T dtoToEntity(E dto);
}
