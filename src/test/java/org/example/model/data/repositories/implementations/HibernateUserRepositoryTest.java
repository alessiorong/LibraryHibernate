package org.example.model.data.repositories.implementations;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.User;
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
    private static final String title = "Il signore delle mosche";
    private static final String genre = "fantasy";
    private User u1;
    private Book b1;
    private Author a1;
    private HibernateUserRepository hur;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
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