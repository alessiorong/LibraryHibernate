package org.example.model.data.repositories;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.data.SessionFactoryHolder;
import org.example.model.data.repositories.implementations.HibernateBookRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBookRepositoryTest {


    private Book b1;
    private Book b2;
    private Book b3;
    private Author a1;
    private Author a2;
    private HibernateBookRepository hr;
    @BeforeEach
    void setUp() {
        a1 = new Author("Ciccio", "Pasticcio", LocalDate.of(2000,1,1), "Italy");
        a2 = new Author("Pippo", "Pluto", LocalDate.of(2001,2,3), "USA");
        b1 = new Book("Piccoli Brividi");
        b2 = new Book("Harry Potter");
        b3 = new Book("Signore degli Anelli");
        b1.setNumPages(100);
        b2.setNumPages(1400);
        b3.setNumPages(1400);
        a1.addBook(b1);
        a2.addBook(b2);
        a2.addBook(b3);
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            s.persist(a1);
            s.persist(a2);
            tr.commit();
        }

     
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            s.remove(a1);
            s.remove(a2);
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
            assertEquals("Piccoli Brividi", b1.getTitle());
        }
    }

    @Test
    void testFindAllByAuthor() {
   
        List<Book> results;
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            results=hr.findAllByAuthor(a2);
            tr.commit();
        }
        assertNotNull(results);
        assertEquals(a2.getBooks().size(), results.size());
        for(Book b : results){
            assertTrue(b.equals(b3) || b.equals(b2));
        }
    }

    @Test
    void testFindAllByAuthorId() {
        List<Book> results;
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            results=hr.findAllByAuthor(a2.getId());
            tr.commit();
        }
        assertNotNull(results);
        assertEquals(a2.getBooks().size(), results.size());
        for(Book b : results){
            assertTrue(b.equals(b3) || b.equals(b2));
        }
    }

    @Test
    void testFindAllByNumPages() {
        List<Book> results;
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            results=hr.findAllByNumPages(1400);
            tr.commit();
        }
        assertNotNull(results);
        assertEquals(a2.getBooks().size(), results.size());
        for(Book b : results){
            assertTrue(b.getNumPages() == 1400);
        }
    }

    @Test
    void testFindAllByTitlePartAndNumPages() {
        List<Book> results;
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            results=hr.findAllByTitlePartAndNumPages(1400,"Anell");
            tr.commit();
        }
        assertNotNull(results);
        assertTrue(results.size()==1);
        assertEquals("Signore degli Anelli",results.get(0).getTitle());
    }

    @Test
    void testGetAuthorAndBookCountById() {
        
    }

    @Test
    void testGetBookCountByAuthorId() {
        int numBooks;
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hr = new HibernateBookRepository(s);
            Transaction tr = s.beginTransaction();
            numBooks= hr.getBookCountByAuthorId(a2.getId());
            tr.commit();
        }
        assertEquals(2,numBooks);
    }

}