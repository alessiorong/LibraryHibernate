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
    private static final String firstname = "Ciccio";
    private static final String lastname = "Pasticcio";
    private static final LocalDate birthdate = LocalDate.of(2000,1,1);
    private static final String country = "Italy";
    private static final String title = "title";
    private static final String title2 = "uno e nessuno";
    private static final String title3 = "Lago delle ninfee";
    private static final String title4 = "Intelligenza emotiva";
    private static final String genre4 = "fantasy";
    private static final String part = "itl";
    private static final int numPages = 280;
    private static final int numPages2 = 500;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private User u;
    private User u1;
    private Author a1;
    private HibernateUserRepository hur;
//CIAU
    @BeforeEach
    void setUp() {
//        a1 = new Author(firstname, lastname, birthdate, country);
//        b1 = new Book(title, numPages);
//        b2 = new Book(title2, numPages2);
//        b3 = new Book(title3, numPages);
        u = new User(firstname);

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
            if(u1!=null){
                s.remove(s.getReference(User.class, u1.getId()));
            }
            tr.commit();
        }
    }

    @Test
    void save() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
        hur = new HibernateUserRepository(s);
        u1 = new User(lastname);
        Transaction tr = s.beginTransaction();
        hur.save(u1);
        tr.commit();
    }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            User u = s.find(User.class, u1.getId());
            assertNotNull(u);
            assertEquals(lastname, u.getUsername());
        }

    }

    @Test
    void findAll() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAll();
            assertTrue(users.stream().anyMatch(uu->uu.getId()==u.getId() && uu.getUsername().equals(u.getUsername())));
            //perche allMatch non funziona?
        }

    }

    @Test
    void findById() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
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
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            assertNull(s.find(User.class, u.getId()));
        }
    }

    @Test
    void update() {
    }

    @Test
    void findAllByAuthorId() {
    }

    @Test
    void findAllWithAtLeastOneBookByGenre() {
    }

    @Test
    void findAllWithAllBooksByGenre() {
    }

    @Test
    void getUsersWithBookCount() {
    }

    @Test
    void testFindAllWithAtLeastOneBookByGenre() {
    }
}