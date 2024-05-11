package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.User;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateUserRepositoryTest {
    private static final String FIRSTNAME = "Ciccio";
    private static final String LASTNAME = "Pasticcio";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String COUNTRY = "Italy";
    private static final String TITLE = "title";
    private static final String TITLE2 = "title2";
    private static final String TITLE3 = "title3";
    private static final String TITLE4 = "title4";
    private static final String PART = "itl";
    private static final String GENRE4 = "fantasy";
    private static final int NUMPAGES = 500;
    private static final int NUMPAGES2 = 280;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Author a1;
    private User u1;
    private User u;
    private HibernateUserRepository hur;

    @BeforeEach
    void setUp() {
//        a1 = new Author(FIRSTNAME, LASTNAME, BIRTHDATE, COUNTRY);
//        b1 = new Book(TITLE, NUMPAGES);
//        b2 = new Book(TITLE2, NUMPAGES2);
//        b3 = new Book(TITLE3, NUMPAGES);
        u = new User(FIRSTNAME);
//        a1.addBook(b1);
//        a1.addBook(b2);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            //s.persist(a1);
            s.persist(u);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            //Author a = s.getReference(Author.class, a1.getId());
            User uu = s.getReference(User.class, u.getId());
            //s.remove(a);
            if (u1 != null){
                s.remove(s.getReference(User.class, u1.getId()));
            }
            tr.commit();
        }
    }

    @Test
    void save() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            u1 = new User(LASTNAME);
            Transaction tr = s.beginTransaction();
            hur.save(u1);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            User u = s.find(User.class, u1.getId());
            assertNotNull(u);
            assertEquals(LASTNAME, u.getUsername());
        }
    }

    @Test
    void findAll() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAll();
            assertTrue(users.stream().anyMatch(uu -> uu.getId() == u.getId()
                    && uu.getUsername().equals(u.getUsername())));
        }
    }

    @Test
    void findById() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            assertTrue(hur.findById(u.getId()).isPresent());
        }
    }

    @Test
    void delete() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            assertNotNull(s.find(User.class, u.getId()));
            Transaction tr = s.beginTransaction();
            hur.delete(u.getId());
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            assertNull(s.find(User.class, u.getId()));
        }
    }

    @Test
    void update() {
    }

    @Test
    void findAllByAuthor() {
    }

    @Test
    void findAllByAtLeastOneGenre() {
    }

    @Test
    void findAllWithOneGenre() {
    }

    @Test
    void findAllWithBookCount() {
    }

    @Test
    void findAllWithAtLeastOneGenre() {
    }
}