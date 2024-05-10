package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {

    private static final String FIRSTNAME = "TEST_FIRSTNAME";
    private static final String LASTNAME = "TEST_LASTNAME";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000,1,1);
    private static final String COUNTRY = "TEST_COUNTRY";
    private static final String TITLE = "TITLE1";
    private static final String TITLE2 = "TITLE2";
    private static final String TITLE3 = "TITLE3";
    private static final String TITLE4 = "TITLE4";
    private static final String TITLE_PART = "ITL";
    private static final int TEST_NUM_PAGES = 100;
    private static final int TEST_NUM_PAGES2 = 200;
    private Book b1;
    private Book b2;
    private Book b3;
    private Book b4;
    private Author a1;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author(FIRSTNAME, LASTNAME, BIRTHDATE, COUNTRY);
        b1 = new Book(TITLE, a1, TEST_NUM_PAGES);
        b2 = new Book(TITLE2, a1, TEST_NUM_PAGES);
        b3 = new Book(TITLE3);
        b4 = new Book(TITLE4, a1, TEST_NUM_PAGES2);
        a1.addBook(b1);
        a1.addBook(b2);
        a1.addBook(b4);
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
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            a1.addBook(b3);
            hr.save(b3);
            tr.commit();
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
        try(Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            Book b = s.find(Book.class, b1.getId());
            assertNotNull(b);
            assertEquals(TITLE, b.getTitle());
            Transaction tr = s.beginTransaction();
            Book updated = new Book(b.getId(), TITLE4);
            hr.update(updated);
            tr.commit();
        }
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            assertEquals(TITLE4, s.find(Book.class, b1.getId()).getTitle());
        }
    }

    @Test
    void findAllByNumPages() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByNumPages(TEST_NUM_PAGES);
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getNumPages() == (b1.getNumPages())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getNumPages() == (b2.getNumPages())));
        }
    }

    @Test
    void findAllByAuthor() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthor(a1);
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getAuthor().getId() == (b1.getAuthor().getId())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getAuthor().getId() == (b2.getAuthor().getId())));
        }
    }

    @Test
    void testFindAllByAuthor() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByAuthor(a1.getId());
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getAuthor().getId() == (b1.getAuthor().getId())));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b2.getId() && b.getAuthor().getId() == (b2.getAuthor().getId())));
        }
    }

    @Test
    void findAllByTitlePartAndNumPages() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            List<Book> books = hr.findAllByTitlePartAndNumPages(TEST_NUM_PAGES, TITLE_PART);
            assertTrue(books.stream().anyMatch(b -> b.getId() == b1.getId() && b.getTitle().contains(TITLE_PART) && b.getNumPages() >= TEST_NUM_PAGES));
            assertTrue(books.stream().anyMatch(b -> b.getId() == b4.getId() && b.getTitle().contains(TITLE_PART) && b.getNumPages() >= TEST_NUM_PAGES));
        }
    }

    @Test
    void getBookCountByAuthorId() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            assertTrue(hr.getBookCountByAuthorId(a1.getId()) == a1.getBooks().size());
        }
    }

    @Test
    void getAuthorAndBookCountById() {
        try (Session s = SessionFactoryHolder.getHolder().createSession()) {
            hr = new HibernateBookRepository(s);
            Object[] result = hr.getAuthorAndBookCountById(a1.getId());
            Author a = (Author) result[0];
            long count = (Long) result[1];
            assertTrue(a.getId() == a1.getId() && count == a1.getBooks().size());
        }
    }
}