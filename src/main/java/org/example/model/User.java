package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_book",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> favouriteBooks = new ArrayList<>();

    public User(){};

    public User(int id, String firstname, String lastname) {
        this(firstname, lastname);
        this.id = id;
    }

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public List<Book> getFavouriteBooks() {
        return favouriteBooks;
    }

    public void setFavouriteBooks(List<Book> favouriteBooks) {
        this.favouriteBooks = favouriteBooks;
    }

    public void addFavouriteBook(Book b){
        if (b != null){
            favouriteBooks.add(b);
            b.getFans().add(this);
        }
    }

}
