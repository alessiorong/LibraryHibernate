package org.example.model.data.repositories;

import java.util.List;
import java.util.Optional;

import org.example.model.User;
import org.hibernate.Session;


public class HibernateUserRepository implements UserRepository{

    private Session session;
    //u.favouriteBooks è solo un libro oppure una lista? 
    private static final String  FIND_ALL_USERS_FOR_FAVORITES_BOOK_AT_LEAST_BOOK_FOR_SAME_AUTHORS="""
                            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks fb WHERE fb.author.id = :id 
          """;
    private static final String  FIND_ALL_USERS_FOR_AT_LEAST_ONE_GENRE="""
                            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks fb WHERE fb.genre = :genre
""";

    @Override
    public User save(User t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<User> findById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void update(User t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<User> findAllUsersForFavoritesBooks(int id) {
        return session.createQuery(FIND_ALL_USERS_FOR_FAVORITES_BOOK_AT_LEAST_BOOK_FOR_SAME_AUTHORS,User.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<User> findAllUsersForAtLeastOneGenre(String genre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllUsersForAtLeastOneGenre'");
    }

}
