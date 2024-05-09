package org.example.model.data.repositories;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateRepository<T> implements Repository<T>{

    private Session session;

    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return List.of();
    }

    @Override
    public Optional<T> findById(int id) {
        return Optional.empty();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(T t) {

    }
}
