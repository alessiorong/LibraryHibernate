package org.example.model.data.repositories;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.repositories.abstractions.BookRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HibernateBookRepository implements BookRepository {
    private Session session;
    private static final String SELECT_BY_NUMPAGES = """
            SELECT b FROM Book b WHERE b.numPages = :numPages
            """;
    private static final String SELECT_BY_AUTHOR = """
            SELECT b FORM Book b WHERE b.author = :author
            """;
    private static final String SELECT_BY_AUTHOR_ID = """
            SELECT b FORM Book b WHERE b.author.id = :id
            """;
    private static final String SELECT_BY_TITLEPART_AND_NUMPAGES = """
            SELECT b FORM Book b WHERE b.numPages >= :numPages AND b.title LIKE :part
            """;
    private static final String SELECT_COUNT_BY_AUTHOR_ID = """
            SELECT COUNT(b) FROM Author a JOIN a.books b WHERE a.id = :id
            """;
    private static final String SELECT_AUTHOR_AND_BOOKCOUNT_BY_AUTHOR_ID = """
            SELECT a,  COUNT(b) FROM Author a JOIN a.books b WHERE a.id = :id
            """;
    public HibernateBookRepository(Session session){
        this.session = session;
    }

    @Override
    public Book create(Book b) {
        session.persist(b);
        return b;
    }

    @Override
    public List<Book> findAll() {
        Query<Book> all = session.createQuery("FROM Book", Book.class);//from la classe, non la colonna sql
        return all.getResultList();
    }

    @Override
    public Optional<Book> findById(Integer id) {
        Book b = session.find(Book.class, id);
        return b != null ? Optional.of(b) : Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        session.remove(session.getReference(Book.class, id));
    }

    @Override
    public void update(Book b) {
        session.merge(b);
    }

    @Override
    public List<Book> findAllByNumPages(int numPages) {
        //return session.createQuery(SELECT_BY_NUMPAGES, Book.class).setParameter("numPages", numPages).getResultList();
        Query<Book> query = session.createQuery(SELECT_BY_NUMPAGES, Book.class);
        query.setParameter("numPages", numPages);
        List<Book> results = query.getResultList();
        return results;
    }

    @Override
    public List<Book> findAllByAuthor(Author a) {
        Query<Book> query = session.createQuery(SELECT_BY_AUTHOR, Book.class);
        query.setParameter("author", a);
        return query.getResultList();
    }

    @Override
    public List<Book> findAllByAuthor(int authorId) {
        return session.createQuery(SELECT_BY_AUTHOR_ID, Book.class).setParameter("author", authorId).getResultList();
    }

    @Override
    public List<Book> findAllByTitlepartAndPages(String part, int numpages) {
        Query<Book> query = session.createQuery(SELECT_BY_TITLEPART_AND_NUMPAGES, Book.class);
        query.setParameter("numPages", numpages).setParameter("part", "%" + part + "%");
        return query.getResultList();
    }

    @Override
    public int getBookCountByAuthorId(int id) {
        return session.createQuery(SELECT_COUNT_BY_AUTHOR_ID, Integer.class).setParameter("id", id).uniqueResult();
    }

    @Override
    public Objects[] getAuthorAndBookCountById(int id) {
        return session.createQuery(SELECT_AUTHOR_AND_BOOKCOUNT_BY_AUTHOR_ID, Objects[].class).setParameter("id", id).uniqueResult();
    }
}


