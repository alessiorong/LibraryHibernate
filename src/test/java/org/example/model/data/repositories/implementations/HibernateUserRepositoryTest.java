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
    private static final String TEST_FIRSTNAME_USER = "TEST_FIRSTNAME_USER";
    private static final String TEST_LASTNAME_USER = "TEST_LASTNAME_USER";
    private static final String TEST_FIRSTNAME_USER2 = "TEST_FIRSTNAME_USER2";
    private static final String TEST_LASTNAME_USER2 = "TEST_LASTNAME_USER2";
    private static final String TEST_FIRSTNAME_USER3 = "TEST_FIRSTNAME_USER3";
    private static final String TEST_LASTNAME_USER3 = "TEST_LASTNAME_USER3";
    private static final String TEST_FIRSTNAME_USER4 = "TEST_FIRSTNAME_USER4";
    private static final String TEST_LASTNAME_USER4 = "TEST_LASTNAME_USER4";
    private static final String TEST_FIRSTNAME_USER5 = "TEST_FIRSTNAME_USER5";
    private static final String TEST_LASTNAME_USER5 = "TEST_LASTNAME_USER5";
    private static final String TEST_FIRSTNAME_AUTHOR = "TEST_FIRSTNAME_AUTHOR";
    private static final String TEST_LASTNAME_AUTHOR = "TEST_LASTNAME_AUTHOR";
    private static final String TEST_FIRSTNAME_AUTHOR2 = "TEST_FIRSTNAME_AUTHOR2";
    private static final String TEST_LASTNAME_AUTHOR2 = "TEST_LASTNAME_AUTHOR2";
    private static final LocalDate TEST_BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String TEST_COUNTRY = "TEST_COUNTRY";
    private static final String TEST_TITLE1 = "TEST_TITLE1";
    private static final String TEST_TITLE2 = "TEST_TITLE2";
    private static final String TEST_TITLE3 = "TEST_TITLE3";
    private static final String TEST_TITLE4 = "TEST_TITLE4";
    private static final String TEST_GENRE1 = "TEST_GENRE1";
    private static final String TEST_GENRE2 = "TEST_GENRE2";
    private static final String TEST_GENRE3 = "TEST_GENRE3";
    private static final int TEST_NUM_PAGES1 = 100;
    private static final int TEST_NUM_PAGES2 = 200;
    private User u1;
    private User u2;
    private User u3;
    private User u4;
    private User u5;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Author a1;
    private Author a2;

    private HibernateUserRepository hr;

    @BeforeEach
    void setUp() {
        u1 = new User(TEST_FIRSTNAME_USER, TEST_LASTNAME_USER);
        u2 = new User(TEST_FIRSTNAME_USER2, TEST_LASTNAME_USER2);
        //U3 NEL SAVE
        u4 = new User(TEST_FIRSTNAME_USER4,TEST_LASTNAME_USER4);
        u5 = new User(TEST_FIRSTNAME_USER5,TEST_LASTNAME_USER5);
        a1 = new Author(TEST_FIRSTNAME_AUTHOR, TEST_LASTNAME_AUTHOR, TEST_BIRTHDATE, TEST_COUNTRY);
        a2 = new Author(TEST_FIRSTNAME_AUTHOR2, TEST_LASTNAME_AUTHOR2, TEST_BIRTHDATE, TEST_COUNTRY);
        b1 = new Book(TEST_TITLE1, a1, TEST_NUM_PAGES1, TEST_GENRE1);
        b2 = new Book(TEST_TITLE2, a1, TEST_NUM_PAGES2, TEST_GENRE2);
        b3 = new Book(TEST_TITLE3, a1, TEST_NUM_PAGES1, TEST_GENRE3);
        b4 = new Book(TEST_TITLE4, a2, TEST_NUM_PAGES2, TEST_GENRE1);
        a1.addBook(b1);
        a1.addBook(b2);
        a1.addBook(b3);
        a2.addBook(b4);
        u1.addFavouriteBook(b1);
        u1.addFavouriteBook(b4);
        u2.addFavouriteBook(b2);
        u4.addFavouriteBook(b4);
        u5.addFavouriteBook(b3);

        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            s.persist(a2);
            s.persist(u1);
            s.persist(u2);
            s.persist(u4);
            s.persist(u5);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            Author a = s.getReference(Author.class, a1.getId());
            Author aa = s.getReference(Author.class, a2.getId());
            if(s.find(User.class, u1.getId())!=null){
                User u=s.find(User.class, u1.getId());
                s.remove(u);
            }
            User uu = s.getReference(User.class, u2.getId());
            User uuuu = s.getReference(User.class, u4.getId());
            User uuuuu = s.getReference(User.class, u5.getId());
            s.remove(a);
            s.remove(aa);
            s.remove(uu);
            s.remove(uuuu);
            s.remove(uuuuu);
            if(u3!=null){
                s.remove(s.getReference(User.class, u3.getId()));
            }

            tr.commit();
        }
    }

    @Test
    void findAllByAuthor() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            List<User> users = hr.findAllByAuthor(a1.getId());
            assertTrue(users.stream().anyMatch(u -> u.getId() == u1.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getAuthor().getId() == a1.getId())));
            assertTrue(users.stream().anyMatch(u -> u.getId() == u2.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getAuthor().getId() == a1.getId())));

        }
    }

    @Test//tutti gli utenti che hanno almeno uno di quei generi in input
    void findAllByAtLeastOneGenre() {//u1(b1) e u4(b4)
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            List<User> users = hr.findAllByAtLeastOneGenre(TEST_GENRE1);
            assertTrue(users.stream().anyMatch(u -> u.getId() == u1.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getGenre().equals(TEST_GENRE1))));
            assertTrue(users.stream().anyMatch(u -> u.getId() == u4.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getGenre().equals(TEST_GENRE1))));
        }
    }

    @Test//
    void findAllWithOneGenre() {
            try (Session s = SessionFactoryHolder.getHolder().createSession()){
                hr = new HibernateUserRepository(s);
                List<User> users = hr.findAllWithOneGenre(TEST_GENRE1);
                assertTrue(users.stream().anyMatch(u -> u.getId() == u1.getId() && u.getFavouriteBooks().stream()
                        .allMatch(b-> b.getGenre().equals(TEST_GENRE1))));
            }
    }

    @Test
    void findAllWithAtLeastOneGenre() {
//        u1.addFavouriteBook(b1);
//        u2.addFavouriteBook(b2);
//        u4.addFavouriteBook(b4);
//        u5.addFavouriteBook(b3);
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            List<User> users = hr.findAllWithAtLeastOneGenre(TEST_GENRE1,TEST_GENRE2,TEST_GENRE3);
            assertTrue(users.stream().anyMatch(u -> u.getId() == u1.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getGenre().equals(TEST_GENRE1) || b.getGenre().equals(TEST_GENRE2)
                            || b.getGenre().equals(TEST_GENRE3))));
            assertTrue(users.stream().anyMatch(u -> u.getId() == u2.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getGenre().equals(TEST_GENRE1) || b.getGenre().equals(TEST_GENRE2)
                            || b.getGenre().equals(TEST_GENRE3))));
            assertTrue(users.stream().anyMatch(u -> u.getId() == u5.getId() && u.getFavouriteBooks().stream()
                    .anyMatch(b-> b.getGenre().equals(TEST_GENRE1) || b.getGenre().equals(TEST_GENRE2)
                            || b.getGenre().equals(TEST_GENRE3))));
        }
    }

    @Test
    void findAllWithBookCount() {//////////////
    }

    @Test
    void save() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            u3 = new User(TEST_FIRSTNAME_USER3, TEST_LASTNAME_USER3);
            hr = new HibernateUserRepository(s);
            Transaction tr = s.beginTransaction();
            u3.addFavouriteBook(b2);
            u3.addFavouriteBook(b4);
            hr.save(u3);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            User u=s.find(User.class, u3.getId());
            assertNotNull(u);
            assertEquals(TEST_FIRSTNAME_USER3, u.getFirstname());
            assertEquals(TEST_LASTNAME_USER3, u.getLastname());
            assertNotNull(u.getFavouriteBooks());
            assertTrue(u.getFavouriteBooks().stream().anyMatch(b -> b.getId() == b2.getId() && b.getTitle().equals(b2.getTitle())));
            assertTrue(u.getFavouriteBooks().stream().anyMatch(b -> b.getId() == b4.getId() && b.getTitle().equals(b4.getTitle())));
        }
    }

    @Test
    void findAll() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateUserRepository(s);
            List<User> users = hr.findAll();
            assertTrue(users.stream().anyMatch(u -> u.getId() == u1.getId() && u.getFirstname().equals(u1.getFirstname())));
            assertTrue(users.stream().anyMatch(u -> u.getId() == u2.getId() && u.getFirstname().equals(u2.getFirstname())));
        }
    }

    @Test
    void findById() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            assertTrue(hr.findById(u1.getId()).isPresent());
        }
    }

    @Test
    void delete() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateUserRepository(s);
            assertNotNull(s.find(User.class,u1.getId()));
            Transaction tr = s.beginTransaction();
            hr.delete(u1.getId());
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            assertNull(s.find(User.class,u1.getId()));
        }
    }

    @Test
    void update() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateUserRepository(s);
            User u = s.find(User.class, u1.getId());
            assertNotNull(u);
            assertEquals(TEST_FIRSTNAME_USER, u.getFirstname());
            Transaction tr = s.beginTransaction();
            User updated = new User(u.getId(),TEST_FIRSTNAME_USER2,TEST_LASTNAME_USER3);
            hr.update(updated);
            tr.commit();
        }
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            assertTrue(TEST_FIRSTNAME_USER2.equals( s.find(User.class, u1.getId()).getFirstname())
                    && TEST_LASTNAME_USER3.equals(s.find(User.class, u1.getId()).getLastname()));
        }
    }
}