package org.example.model.data.repositories.implementations;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.User;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HibernateUserRepositoryTest {
    private static final String CATEGORY1="CATEGORYTEST1";
    private static final String CATEGORY2="CATEGORYTEST2";
    private static final String CATEGORY3="CATEGORYTEST3";
    private User u1;
    private User u2;
    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private Author a2;
    private HibernateUserRepository hr;

    @BeforeEach
    void setUp() {
        u1 = new User("Pippo");
        u2 = new User("Pluto");
        a1 = new Author("Ciccio", "Pasticcio", LocalDate.of(2000,1,1), "Italy");
        a2 = new Author("Pippo", "Pluto", LocalDate.of(2001,2,3), "USA");
        b1 = new Book("Piccoli Brividi");
        b2 = new Book("Harry Potter");
        b3 = new Book("Signore degli Anelli");

        b1.setGenre(CATEGORY1);
        b2.setGenre(CATEGORY2);
        b3.setGenre(CATEGORY3);

        a1.addBook(b1);
        a1.addBook(b2);
        a2.addBook(b3);

        u1.addFavouriteBook(b1);
        u2.addFavouriteBook(b2);
        u2.addFavouriteBook(b3);

        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            Transaction tr = s.beginTransaction();
            s.persist(u1);
            s.persist(u2);
            s.persist(a1);
            s.persist(a2);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {

        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            Transaction tr = s.beginTransaction();
            s.remove(u1);
            s.remove(u2);
            s.remove(a1);
            s.remove(a2);
            tr.commit();
        }
    }

    @Test
    void testFindAllByAtLeastOneGenre() {
        List<User> results;

        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
             hr = new HibernateUserRepository(s);
            results = hr.findAllByAtLeastOneGenre(CATEGORY2);
        }
      assertTrue(results.size()==1);
      assertEquals(u2.getId(), results.get(0).getId());
    }

    @Test
    void testFindAllByAuthor() {
        List<User> results;

        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateUserRepository(s);
            results = hr.findAllByAuthor(a1.getId()); 
       }
       assertNotNull(results);
       assertTrue(results.stream().anyMatch(u -> u.getId()==u1.getId()));
       assertTrue(results.stream().filter(u -> u.getId() == u1.getId())
       .anyMatch(u -> u.getFavouriteBooks().stream().anyMatch(b -> b.getAuthor().getId()==a1.getId())));
    }

    @Test
    void testFindAllWithAtLeastOneGenre() {
        List<User> results;
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateUserRepository(s);
            results = hr.findAllWithAtLeastOneGenre(b2.getGenre(),"NOCATEGORY1","NOCATEGORY2"); 
            assertTrue(results.size()==1);
            assertEquals(u2.getId(),results.get(0).getId());
       }

    }

    @Test
    void testFindAllWithBookCount() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateUserRepository(s);
            //List<Object[]> results = hr.findAllWithBookCount();
            // var users = results.stream().filter(this::isTestUser).map(UserAndFavoritesCount::fromObjectArray).toList();
            // assertEquals(u2.getId(), users.get(0).getUser().getId());
            // assertEquals(u2.getFavoriteCount(),users.get(0).getFavoriteCount());
            // assertEquals(u1.getId(), users.get(1).getUser().getId());
            // assertEquals(u1.getFavoriteCount(),users.get(1).getFavoriteCount());
            List<UserAndFavoritesCount> results = hr.findAllWithBookCount();
       }

    }

    @Test
    void testFindAllWithOneGenre() {

    }

    private boolean isTestUser(Object[] o){
        User u = (User)o[0];
        return u.getId()==u1.getId() || u.getId()==u2.getId();
    }

}
