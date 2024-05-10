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
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "birthdate", columnDefinition = "date")
    private LocalDate birthdate;
    @Column(name = "country")
    private String country;
    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE,CascadeType.PERSIST})
    private List<Book> books = new ArrayList<>();

    public Author(int id, String firstname, String lastname, LocalDate birthdate,
                  String country, List<Book> books) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.country = country;
        this.books = books;
    }

    public Author(){

    }

    public Author(String firstname, String lastname, LocalDate birthdate, String country) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.country = country;
    }

    public void addBook(Book b){
        b.setAuthor(this);
        books.add(b);
    }

    public List<Book> getBooks() {
        return books;
    }

    public int getId() {
        return id;
    }

    
    
}
