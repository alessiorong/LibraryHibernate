package org.example.model.data.repositories;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {
    private static final String firstname = "Ciccio";
    private static final String lastname = "Pasticcio";
    private static final LocalDate birthdate = LocalDate.of(1947,12,3);
    private static final String country = "Italia";
    private static final String title = "Il Signore delle mosche";
    private Book b1;
    private Author a1;
    private HibernateBookRepository hbr;
    @BeforeEach
    void setUp() {
        a1 = new Author(firstname,lastname,birthdate,country);
        b1 = new Book(title);
        a1.addBook(b1);
    }

    @AfterEach
    void tearDown() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            Transaction tr = s.beginTransaction();
            s.remove(a1);
            tr.commit();
        }
    }

    @Test
    void create() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hbr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            hbr.create(b1);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Book b = s.find(Book.class, b1.getId());
            assertNotNull(b);
            assertEquals(title, b1.getTitle());
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