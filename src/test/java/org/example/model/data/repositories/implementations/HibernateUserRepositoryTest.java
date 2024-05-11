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
    private static final String firsname2 = "Pino";
    private static final String firstname4 = "Gino";
    private static final LocalDate birthdate = LocalDate.of(2000,1,1);
    private static final String country = "Italy";
    private static final String title = "title";
    private static final String title2 = "uno e nessuno";
    private static final String title3 = "Lago delle ninfee";
    private static final String title4 = "Intelligenza emotiva";
    private static final String genre = "fantasy";
    private static final String genre1 = "saggistica";
    private static final String genre2 = "narrativa";
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private User u;
    private User us;
    private User u1;
    private Author a1;
    private HibernateUserRepository hur;

    @BeforeEach
    void setUp() {
        a1 = new Author(firstname, lastname, birthdate, country);
        b1 = new Book(title, genre);
        b2 = new Book(title2, genre);
        b3 = new Book(title3, genre1);
        b4 = new Book(title4, genre1);
        u = new User(firstname);
        us = new User(firsname2);

        a1.addBook(b1);
        a1.addBook(b2);
        a1.addBook(b3);
        a1.addBook(b4);
        u.addBook(b1);
        u.addBook(b2);
        us.addBook(b2);
        us.addBook(b4);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            s.persist(u);
            s.persist(us);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            User user = s.getReference(User.class, us.getId());
            Author a = s.getReference(Author.class, a1.getId());
            s.remove(user);
            s.remove(a);
            if(s.find(User.class, u.getId())!=null){
                User uu = s.getReference(User.class, u.getId());
                s.remove(uu);
            }
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
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            User uu =s.find(User.class, u.getId());
            assertNotNull(uu);
            assertEquals(firstname, uu.getUsername());
            Transaction tr = s.beginTransaction();
            User u4 = new User(uu.getId(), firstname4);
            hur.update(u4);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            assertEquals(firstname4, s.find(User.class, u.getId()).getUsername());
        }
    }

    @Test
    void findAllByAuthorId() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAllByAuthorId(a1.getId());
            assertTrue(users.stream().allMatch(u->u.getFavouriteBooks().stream().anyMatch(b->b.getAuthor().getId()==a1.getId())));
        }
    }

    @Test
    void findAllWithAtLeastOneBookByGenre() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAllWithAtLeastOneBookByGenre(genre);
            assertTrue(users.stream().allMatch(u->u.getFavouriteBooks().stream().anyMatch(b->b.getGenre().equals(genre))));
        }
    }

    @Test
    void findAllWithAllBooksByGenre() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAllWithAllBooksByGenre(genre);
            assertTrue(users.stream().allMatch(u->u.getFavouriteBooks().stream().allMatch(b->b.getGenre().equals(genre))));
        }
    }

    @Test
    void testFindAllWithAtLeastOneBookByGenre() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            List<User> users = hur.findAllWithAtLeastOneBookByGenre(genre,genre1,genre2);
            assertTrue(users.stream().allMatch(u->u.getFavouriteBooks().stream().anyMatch(b->b.getGenre().equals(genre)||b.getGenre().equals(genre1)||b.getGenre().equals(genre2))));
        }

    }

    @Test
    void getUsersWithBookCount() {
    }
}