package org.example.model.data.repositories.abstractions;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T save(T t);
    List<T> findAll();
    Optional<T> findById(ID id);
    void delete(ID id);
    void update(T t);
}
