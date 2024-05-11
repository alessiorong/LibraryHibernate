package org.example.model.data.repositories.abstractions;

import org.example.model.Author;
import org.example.model.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer>{
    List<Book> findAllByNumPages(int numPages);
    List<Book> findAllByAuthor(Author author);
    List<Book> findAllByAuthorId(int id);
    List<Book> findAllByTitlePartAndNumPages(int numPages, String part);
    int getBookCountByAuthorId(int id);
    Object[] getAuthorAndBookCountById(int id);
}
