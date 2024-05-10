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
    private static final String title4 = "Intelligenza emotiva";
    private static final String genre4 = "fantasy";
    private static final int numPages = 280;
    private static final int numPages2 = 500;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Author a1;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author(firstname, lastname, birthdate, country);
        b1 = new Book(title, numPages);
        b2 = new Book(title2, numPages2);
        b3 = new Book(title3, numPages);
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
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Book bb =s.find(Book.class, b1.getId());
            assertNotNull(bb);
            assertEquals(title, bb.getTitle());
            Transaction tr = s.beginTransaction();
            Book b4 = new Book(bb.getId(), title4);
            hr.update(b4);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            assertEquals(title4, s.find(Book.class, b1.getId()).getTitle());
        }
    }

    @Test
    void findAllByNumPages() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByNumPages(numPages);
            for (Book b : books){
                assertEquals(b.getNumPages(), numPages);
            }
        }
    }

    @Test
    void findAllByAuthor() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthor(a1);
            for (Book b : books){
                assertEquals(b.getAuthor().getId(), a1.getId());
            }
        }
    }

    @Test
    void testFindAllByAuthorId() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthorId(a1.getId());
            for (Book b : books){
                assertEquals(b.getAuthor().getId(), a1.getId());
            }
        }
    }

    @Test
    void findAllByTitlePartAndNumPages() {
    }

    @Test
    void getBookCountByAuthorId() {
    }

    @Test
    void getAuthorAndBookCountById() {
    }
}