package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.repositories.abstractions.BookRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateBookRepository implements BookRepository {

    private Session session;
    private static final String SELECT_BY_NUM_PAGES = """
            SELECT b FROM Book b WHERE b.numPages = :numPages 
            """; // :numPages in hql

    private static final String SELECT_BY_AUTHOR = """
            SELECT b FROM Book b WHERE b.author = :author
            """;

    private static final String SELECT_BY_AUTHOR_ID = """
            SELECT b FROM Book b WHERE b.author.id = :id
            """;

    private static final String SELECT_BY_TITLE_PART_AND_NUM_PAGES = """
            SELECT b FROM Book b WHERE b.numPages >= :numPages AND b.title LIKE :part
            """;

    private static final String SELECT_COUNT_BY_AUTHOR_ID = """
            SELECT COUNT(b) FROM Author a JOIN a.books b WHERE a.id = :id
            """;

    private static final String SELECT_AUTHOR_AND_BOOK_COUNT_BY_AUTHOR_ID = """
            SELECT a, COUNT(b) FROM Author a JOIN a.books b WHERE a.id = :id
            """;
    
    public HibernateBookRepository(Session session){
        this.session = session;
    }

    @Override
    public Book save(Book b) {
        session.persist(b);
        return b;
    }

    @Override
    public List<Book> findAll() {
        Query<Book> all = session.createQuery("FROM Book", Book.class);
        return all.getResultList();

    }

    @Override
    public Optional<Book> findById(int id) {
      Book b = session.find(Book.class, id);
      return b != null ? Optional.of(b) : Optional.empty();
    }

    @Override
    public void delete(int id) {
        Book b = session.getReference(Book.class, id);
        // Book b2 = session.get(Book.class, id);
        session.remove(b);
    }

    @Override
    public void update(Book b) {
        session.merge(b);
    }

    @Override
    public List<Book> findAllByNumPages(int numPages) {
        //versione più formattata
        return session.createQuery(SELECT_BY_NUM_PAGES, Book.class)
                .setParameter("numPages", numPages)
                .getResultList();
//        Query<Book> query = session.createQuery(SELECT_BY_NUM_PAGES, Book.class);
//        query.setParameter("numPages", numPages);
//        List<Book> results = query.getResultList();
//        return results;
    }

    @Override
    public List<Book> findAllByAuthor(Author author) {
        return session.createQuery(SELECT_BY_AUTHOR, Book.class)
                .setParameter("author", author)
                .getResultList();
    }

    @Override
    public List<Book> findAllByAuthorId(int id) {
        return session.createQuery(SELECT_BY_AUTHOR_ID, Book.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Book> findAllByTitlePartAndNumPages(int numPages, String part) {
        return session.createQuery(SELECT_BY_TITLE_PART_AND_NUM_PAGES, Book.class)
                .setParameter("numPages", numPages)
                .setParameter("part", "%" + part + "%")
                .getResultList();
    }

    @Override
    public int getBookCountByAuthorId(int id) {
        return session.createQuery(SELECT_COUNT_BY_AUTHOR_ID, Integer.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Object[] getAuthorAndBookCountById(int id) {
        return session.createQuery(SELECT_AUTHOR_AND_BOOK_COUNT_BY_AUTHOR_ID, Object[].class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
