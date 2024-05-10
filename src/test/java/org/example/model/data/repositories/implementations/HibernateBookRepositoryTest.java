package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.User;
import org.example.model.data.SessionFactoryHolder;
import org.example.model.data.repositories.implementations.HibernateBookRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {

    private static final String firstname = "Ciccio";
    private static final String lastname = "Pasticcio";
    private static final LocalDate birthdate = LocalDate.of(2000,1,1);
    private static final String country = "Italy";
    private static final String title = "title";
    private static final String title2 = "uno e nessuno";
    private static final String title3 = "Lago delle ninfee";
    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author(firstname, lastname, birthdate, country);
        b1 = new Book(title);
        b2 = new Book(title2);
        b3 = new Book(title3);
        a1.addBook(b1);
        a1.addBook(b2);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
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
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            a1.addBook(b3);
            s.persist(a1);
            hr.save(b3);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Book b = s.find(Book.class, b3.getId());
            assertNotNull(b);
            assertEquals(title3, b.getTitle());
            assertNotNull(b.getAuthor());
            assertEquals(a1.getId(), b.getAuthor().getId());
        }
    }

    @Test
    void findAll() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAll();
            assertTrue(books.stream().anyMatch(b->b.getId()==b1.getId() && b.getTitle().equals(b1.getTitle())));
            assertTrue(books.stream().anyMatch(b->b.getId()==b2.getId() && b.getTitle().equals(b2.getTitle())));
        }
    }

    @Test
    void findById() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            assertTrue(hr.findById(b1.getId()).isPresent());
        }
    }

    @Test
    void delete() {
            try(Session s = SessionFactoryHolder.getHolder().createSession()){
                hr = new HibernateBookRepository(s);
                assertNotNull(s.find(Book.class, b1.getId()));
                Transaction tr = s.beginTransaction();
                hr.delete(b1.getId());
                tr.commit();
            }
            try(Session s = SessionFactoryHolder.getHolder().createSession()) {
                assertNull(s.find(Book.class, b1.getId()));
            }
    }

    @Test
    void update() {
    }
}