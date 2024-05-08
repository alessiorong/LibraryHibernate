package org.example.model.data.repositories;

import org.example.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book b);
    List<Book> findAll();
    Optional<Book> findById(int id);
    void delete(int id);
    void update(Book b);
}
