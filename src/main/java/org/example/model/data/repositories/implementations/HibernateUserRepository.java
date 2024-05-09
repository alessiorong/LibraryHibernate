package org.example.model.data.repositories.implementations;

import org.hibernate.query.Query;
import org.example.model.User;
import org.example.model.data.repositories.abstractions.UserRepository;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateUserRepository  implements UserRepository {

    private Session session;

    private static final String SELECT_BY_FAVOURITE_BY_AUTHOR_ID = """
        SELECT u FROM User u JOIN u.favouriteBooks fb
        WHERE fb.author.id = :id
        """;

    private static final String SELECT_BY_AT_LEAST_ONE_FAVOURITE_BY_GENRE = """
        SELECT DISTINCT u FROM User u JOIN u.favouriteBooks fb
        WHERE fb.genre = :genre
        """;

    private static final String SELECT_BY_ALL_FAVOURITE_SAME_GENRE = """
        SELECT u FROM User u
        WHERE (SELECT COUNT(b) FROM u.favoutiteBooks WHERE b.genre = :genre)
        = (SELECT COUNT(b) FROM u.favoutiteBooks)
        """;

    private static final String SELECT_USER_AND_BOOK_COUNT = """
            SELECT u, COUNT(b) FROM User JOIN u.favouriteBooks fb
            GROUP BY u ORDER BY COUNT(b) DESC
            """;

    private static final String SELECT_AT_LEAST_ONE_FAVOURITE_BY_3_GENRE = """
        SELECT DISTINCT u FROM User u JOIN u.favouriteBooks fb
        WHERE fb.genre = :genre OR fb.genre = :genre2 OR fb.genre = :genre3
        """;

    public HibernateUserRepository(Session session){
        this.session = session;
    }

    @Override
    public User save(User u) {
        session.persist(u);
        return u;
    }

    @Override
    public List<User> findAll() {
        Query<User> all = session.createQuery("FROM User", User.class);
        return all.getResultList();
    }

    @Override
    public Optional<User> findById(int id) {
        User u = session.find(User.class, id);
        return u != null ? Optional.of(u) : Optional.empty();
    }

    @Override
    public void delete(int id) {
        User u = session.getReference(User.class, id);
        session.remove(u);
    }

    @Override
    public void update(User user) {
        session.merge(user);
    }

    @Override
    public List<User> findAllByAuthorId(int id) {
        return session.createQuery(SELECT_BY_FAVOURITE_BY_AUTHOR_ID, User.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<User> findAllWithAtLeastOneBookByGenre(String genre) {
        return session.createQuery(SELECT_BY_AT_LEAST_ONE_FAVOURITE_BY_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<User> findAllWithAllBooksByGenre(String genre) {
        return session.createQuery(SELECT_BY_ALL_FAVOURITE_SAME_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<Object[]> getUsersWithCountBooks() {
        return session.createQuery(SELECT_USER_AND_BOOK_COUNT, Object[].class)
                .getResultList();
    }

    @Override
    public List<User> findAllWithAtLeastOneBookByGenre(String genre, String genre2, String genre3) {
        return session.createQuery(SELECT_AT_LEAST_ONE_FAVOURITE_BY_3_GENRE, User.class)
                .setParameter("genre", genre)
                .setParameter("genre2", genre2)
                .setParameter("genre3", genre3)
                .getResultList();
    }


}
