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
    @Column(name = "genre")
    private String genre;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Column(name = "num_ pages")
    private int numPages;
    @ManyToMany(mappedBy = "favouriteBooks")
    private List<User> fans = new ArrayList<>();

    public Book() {
    }

    public Book(String title) {
        this.title = title;
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

    public static void main(String[] args) {
        Author a = new Author();
        Book b = new Book();
        a.addBook(b);
        // b.setAuthor(a);
        // Uso Hibernate per salvare il libro e l'autore
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (other.id != id) {
            return false;
        }

        return true;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getNumPages() {
        return numPages;
    }

    

}
