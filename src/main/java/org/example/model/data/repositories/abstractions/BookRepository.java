package org.example.model.data.repositories.abstractions;

import org.example.model.Author;
import org.example.model.Book;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findAllByNumPages(int numPages);
    List<Book> findAllByAuthor(Author a);
    List<Book> findAllByAuthor(int authorId);
    List<Book> findAllByTitlepartAndPages(String part, int numpages);
    int getBookCountByAuthorId (int id);
    Objects[] getAuthorAndBookCountById (int id);
}
