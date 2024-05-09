package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "fistname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "birthdate", columnDefinition = "date")
    private LocalDate birthdate;
    @Column(name = "country")
    private String country;
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE) //mappatura è in author
    private List<Book> books = new ArrayList<>();

    public Author(){}

    public Author(int id, String firstname, String lastname, LocalDate birthdate, String country, List<Book> books) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.country = country;
        this.books = books;
    }

    public Author(String firstname, String lastname, LocalDate birthdate, String country) {
        this(0, firstname, lastname, birthdate, country, null);
    }

    public void addBook(Book b){
        books.add(b);
        b.setAuthor(this);
    }
}
