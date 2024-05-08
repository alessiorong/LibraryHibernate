package org.example.model.data.repositories;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.example.model.data.repositories.implementations.HibernateBookRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {

    private static final String FIRSTNAME = "Ciccio";
    private static final String LASTNAME = "Pasticcio";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String COUNTRY = "Italy";
    private static final String TITLE = "title";
    private Book b1;
    private Author a1;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author(FIRSTNAME, LASTNAME, BIRTHDATE, COUNTRY);
        b1 = new Book(TITLE);
        a1.addBook(b1);
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            s.remove(a1);
            tr.commit();
        }
    }

    @Test
    void save() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            hr.save(b1);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Book b = s.find(Book.class, b1.getId());
            assertNotNull(b);
            assertEquals(TITLE, b1.getTitle());
        }
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