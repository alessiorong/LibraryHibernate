package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column(name = "num_pages")
    private int numPages;
    @Column(name = "genre")
    private String genre;
    @Transient //hibernate lo ignorerà
    private int dummy;


    @ManyToMany(mappedBy = "favouriteBooks")
    private List<User> fans = new ArrayList<>();

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }
    public Book(String title, Author author, int numPages){
        this(title);
        this.author = author;
        this.numPages = numPages;
    }
    public Book(int id, String title){
        this(title);
        this.id = id;

    }
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getFans() {
        return fans;
    }
    public int getNumPages(){
        return numPages;
    }
    public static void main(String[] args) {
        Author a = new Author();
        Book b = new Book();
        a.addBook(b);
        //b.setAuthor(a);
        //Uso Hibernate per salvare il libro e l'autore
    }
    @Override
    public String toString(){
        return id + title;
    }
}
// a