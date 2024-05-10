package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {

    private static final String FIRSTNAME = "Ciccio";
    private static final String LASTNAME = "Pasticcio";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String COUNTRY = "Italy";
    private static final String TITLE = "title";
    private static final String TITLE2 = "title2";
    private static final String TITLE3 = "title3";
    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author(FIRSTNAME, LASTNAME, BIRTHDATE, COUNTRY);
        b1 = new Book(TITLE);
        b2 = new Book(TITLE2);
        b3 = new Book(TITLE3);
        a1.addBook(b1);
        a1.addBook(b2);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
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
    void save() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            a1.addBook(b3);
            hr.save(b3);
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Book b = s.find(Book.class, b3.getId());
            assertNotNull(b);
            assertEquals(TITLE3, b.getTitle());
            assertNotNull(b.getAuthor());
            assertEquals(a1.getId(), b.getAuthor().getId());
        }
    }

    @Test
    void findAll() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAll();
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getTitle().equals(b1.getTitle())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getTitle().equals(b2.getTitle())));
        }
    }

    @Test
    void findById() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
//            Book b = s.find(Book.class, b1.getId());
//            assertNotNull(b);
//            assertEquals(TITLE, b1.getTitle());
//            Optional<Book> ob = hr.findById(b1.getId());
//            assertEquals(b.getId(), ob.get().getId());
            assertTrue(hr.findById(b1.getId()).isPresent());
        }
    }

    @Test
    void delete() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            assertNotNull(s.find(Book.class,b1.getId()));
            Transaction tr = s.beginTransaction();
            hr.delete(b1.getId());
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            assertNull(s.find(Book.class,b1.getId()));
        }
    }

    @Test
    void update() {
    }
}