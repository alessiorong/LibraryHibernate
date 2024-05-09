package org.example.model.data.repositories;

import org.example.model.Book;

import java.util.List;
import java.util.Optional;

public interface Repository <T>{

    T save(T t);
    List<T> findAll();
    Optional<T> findById(int id);
    void delete(int id);
    void update(T t);
}
