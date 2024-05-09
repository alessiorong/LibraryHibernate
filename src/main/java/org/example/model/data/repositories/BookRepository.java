package org.example.model.data.repositories;

import org.example.model.Author;
import org.example.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book>{
    Book save(Book b);
    List<Book> findAll();
    Optional<Book> findById(int id);
    void delete(int id);
    void update(Book b);
    List<Book> findAllByNumPages(int numPages);
    List<Book> findAllByAuthor(Author author);
    List<Book> findAllByAuthor(int id);
    List<Book> findAllByTitlePartAndNumPages(int numPages, String part);
    int getBookCountByAuthorId(int id);
    Object[] getAuthorAndBookCountById(int id);
}
