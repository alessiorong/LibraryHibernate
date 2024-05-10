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
    @Column(name = "username")
    private String username;
    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "user_book",
    // joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "book_id"))
    @ManyToMany(mappedBy = "fans", cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Book> favouriteBooks = new ArrayList<>();

    public User(){};

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User( String username) {
        this.username = username;
    }

    public int getFavoriteCount(){
        return favouriteBooks.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
