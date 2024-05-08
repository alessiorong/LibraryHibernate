package org.example.model.data.repositories;

import org.example.model.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateBookRepository implements BookRepository{

    private Session session;
    
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
}
