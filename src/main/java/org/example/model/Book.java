package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToMany(mappedBy = "favouriteBooks")
    private List<User> fans = new ArrayList<>();

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static void main(String[] args) {
        Author a = new Author();
        Book b = new Book();
        a.addBook(b);
        //b.setAuthor(a);
        //Uso Hibernate per salvare il libro e l'autore
    }

}
