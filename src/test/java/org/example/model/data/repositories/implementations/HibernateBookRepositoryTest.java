package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {

    private static final String FIRSTNAME = "Ciccio";
    private static final String LASTNAME = "Pasticcio";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final String COUNTRY = "Italy";
    private static final String TITLE = "title";
    private static final String TITLE2 = "title2";
    private static final String TITLE3 = "title3";
    private static final String TITLE4 = "title4";
    private static final int NUMPAGES = 100;
    private static final int AUTHOR_ID = 1;
    private static final String TITLE_PART = "itl";
    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private HibernateBookRepository hr;

    @BeforeEach
    void setUp() {
        a1 = new Author(FIRSTNAME, LASTNAME, BIRTHDATE, COUNTRY);
        b1 = new Book(TITLE, a1, NUMPAGES);
        b2 = new Book(TITLE2, a1, NUMPAGES);
        b3 = new Book(TITLE3);
        a1.addBook(b1);
        a1.addBook(b2);
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            tr.commit();
        }
    }

    @AfterEach
    void tearDown() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            Transaction tr = s.beginTransaction();
            Author a = s.getReference(Author.class, a1.getId());
            s.remove(a);
            tr.commit();
        }
    }

    @Test
    void save() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            a1.addBook(b3);
            hr.save(b3);
            tr.commit();
        }
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            Book b = s.find(Book.class, b3.getId());
            assertNotNull(b);
            assertEquals(TITLE3, b.getTitle());
            assertNotNull(b.getAuthor());
            assertEquals(a1.getId(), b.getAuthor().getId());
        }
    }

    @Test
    void findAll() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAll();
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getTitle().equals(b1.getTitle())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getTitle().equals(b2.getTitle())));
        }
    }

    @Test
    void findById() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
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
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            assertNotNull(s.find(Book.class, b1.getId()));
            Transaction tr = s.beginTransaction();
            hr.delete(b1.getId());
            tr.commit();
        }
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            assertNull(s.find(Book.class, b1.getId()));
        }
    }

    @Test
    void update() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            Book b = s.find(Book.class, b1.getId());
            assertNotNull(b);
            assertEquals(TITLE, b.getTitle());
            Transaction tr = s.beginTransaction();
            Book updated = new Book(b1.getId(), TITLE4);
            hr.update(updated);
            tr.commit();
        }
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            assertEquals(s.find(Book.class, b1.getId()).getTitle(), TITLE4);

        }
    }

    @Test
    void findAllByNumPages() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByNumPages(NUMPAGES);
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getNumPages() == (b1.getNumPages())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getNumPages() == (b2.getNumPages())));
        }
    }

    @Test
    void findAllByAuthor() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthor(a1);
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getAuthor().getId() == (b1.getAuthor().getId())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getAuthor().getId() == (b2.getAuthor().getId())));
        }
    }

    @Test
    void testFindAllByAuthor() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthor(a1.getId());
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getAuthor().getId() == (b1.getAuthor().getId())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getAuthor().getId() == (b2.getAuthor().getId())));
        }
    }

//    @Test
//    void findAllByTitlePartAndNumPages() {
//        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
//            hr = new HibernateBookRepository(s);
//            List<Book> books = hr.findAllByTitlePartAndNumPages();
//            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getAuthor().getId() == (b1.getAuthor().getId())));
//            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getAuthor().getId() == (b2.getAuthor().getId())));
//        }
//    }

    @Test
    void getBookCountByAuthorId() {
    }

    @Test
    void getAuthorAndBookCountById() {
    }
}