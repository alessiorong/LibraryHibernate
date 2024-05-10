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

class HibernateUserRepositoryTest {
    private static final String TEST_FIRSTNAME_USER = "TEST_FIRSTNAME_USER";
    private static final String TEST_LASTNAME_USER = "TEST_LASTNAME_USER";
    private static final String TEST_FIRSTNAME_AUTHOR = "TEST_FIRSTNAME_AUTHOR";
    private static final String TEST_LASTNAME_AUTHOR = "TEST_LASTNAME_AUTHOR";
    private static final LocalDate TEST_BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String TEST_COUNTRY = "TEST_COUNTRY";
    private static final String TEST_TITLE1 = "TEST_TITLE1";
    private static final String TEST_TITLE2 = "TEST_TITLE2";
    private static final String TEST_TITLE3 = "TEST_TITLE3";
    private static final String TEST_GENRE1 = "TEST_GENRE1";
    private static final String TEST_GENRE2 = "TEST_GENRE2";
    private static final String TEST_GENRE3 = "TEST_GENRE3";
    private static final int TEST_NUM_PAGES1 = 100;
    private static final int TEST_NUM_PAGES2 = 200;
    private User u1;
    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private HibernateUserRepository hur;

    @BeforeEach
    void setUp() {
        User u1 = new User(TEST_FIRSTNAME_USER, TEST_LASTNAME_USER);
        Author a1 = new Author(TEST_FIRSTNAME_AUTHOR, TEST_LASTNAME_AUTHOR, TEST_BIRTHDATE, TEST_COUNTRY);
        Book b1 = new Book(TEST_TITLE1, a1, TEST_NUM_PAGES1, TEST_GENRE1);
        Book b2 = new Book(TEST_TITLE2, a1, TEST_NUM_PAGES2, TEST_GENRE2);
        Book b3 = new Book(TEST_TITLE3, a1, TEST_NUM_PAGES1, TEST_GENRE3);
        a1.addBook(b1);
        a1.addBook(b2);
        a1.addBook(b3);
        u1.addFavouriteBook(b1);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(u1);
            s.persist(a1);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            Author a = s.getReference(Author.class, a1.getId());
            s.remove(a);
            tr.commit();
        }
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

    @Test
    void save() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}