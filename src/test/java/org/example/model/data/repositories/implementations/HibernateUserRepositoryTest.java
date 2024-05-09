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

import static org.junit.jupiter.api.Assertions.*;

class HibernateUserRepositoryTest {


    private static final String firstname = "Ciccio";
    private static final String lastname = "Pasticcio";
    private static final LocalDate birthdate = LocalDate.of(2000,1,1);
    private static final String country = "Italy";
    private static final String title = "La cacca rosa";
    private static final String genre = "horror";
    private Book b1;
    private Author a1;
    private HibernateUserRepository hur;
    private User u1;

    @BeforeEach
    void setUp() {
        u1 = new User(firstname);
        a1 = new Author(firstname, lastname, birthdate, country);
        b1 = new Book(title, genre);
        a1.addBook(b1);
        u1.addFavBook(b1);
    }

    @AfterEach
    void tearDown() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            Transaction tr = s.beginTransaction();
            s.remove(a1);
            s.remove(u1);
            tr.commit();
        }
    }

    @Test
    void testSave() {
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            hur = new HibernateUserRepository(s);
            Transaction tr = s.beginTransaction();
            s.persist(b1);
            s.persist(a1);
            hur.save(u1);
            tr.commit();
        }
        try(Session s = SessionFactoryHolder.getHolder().createSession()){
            User u = s.find(User.class, u1.getId());
            assertNotNull(u);
            assertEquals(firstname, u1.getUsername());
        }
    }

    @Test
    void testFindAll() {
    }

    @Test
    void testFindById() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testFindAllByAuthorId() {
    }

    @Test
    void testFindAllWithAtLeastOneBookByGenre1() {
    }

    @Test
    void testFindAllWithAllBooksByGenre() {
    }

    @Test
    void testGetUsersWithCountBooks() {
    }

    @Test
    void testFindAllWithAtLeastOneBookByGenre2() {
    }
}