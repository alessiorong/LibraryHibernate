package org.example.model.data.repositories.implementations;

import org.example.model.User;
import org.example.model.data.repositories.abstractions.UserRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class HibernateUserRepository implements UserRepository {

    private Session session;

    private static final String SELECT_BY_AUTHOR_ID = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b WHERE b.author.id = :authorid
            """;
    private static final String SELECT_BY_AT_LEAST_ONE_GENRE = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b WHERE b.genre = :genre
            """;
    private static final String SELECT_BY_SINGLE_GENRE = """
            SELECT u FROM User u WHERE (
            SELECT COUNT(b) FROM u.favouriteBooks as b
            WHERE b.genre = :genre) =
            (SELECT COUNT(b) FROM u.favouriteBooks as b)
            """;
    private static final String SELECT_USERS_AND_BOOKS_COUNT = """
            SELECT u, COUNT(u.favouriteBooks) AS c FROM User u JOIN u.favouriteBooks 
            GROUP BY u 
            ORDER BY c DESC
            """;
    private static final String SELECT_USERS_AND_BOOKS_COUNT_CUSTOM = """
            SELECT new org.example.model.data.repositories.implementations.UserAndFavoritesCount(u,COUNT(b))
            FROM User u JOIN u.favouriteBooks b
            GROUP BY u 
            """;
    private static final String SELECT_BY_GENRE_IN = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b 
            WHERE b.genre IN (:genre1, :genre2, :genre3)
            """;
    private static final String SELECT_BY_GENRE_IN_V2 = """
            SELECT u FROM User u
            WHERE EXISTS (
                SELECT 1
                FROM u.favouriteBooks as b
                WHERE b.genre IN 
                (:genre1, :genre2, :genre3)
            )
            """;

    public HibernateUserRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<User> findAllByAuthor(int authorId) {
        return session.createQuery(SELECT_BY_AUTHOR_ID, User.class)
                .setParameter("authorid", authorId)
                .getResultList();
    }

    @Override
    public List<User> findAllByAtLeastOneGenre(String genre) {
        return session.createQuery(SELECT_BY_AT_LEAST_ONE_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<User> findAllWithOneGenre(String genre) {
        return session.createQuery(SELECT_BY_SINGLE_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<UserAndFavoritesCount> findAllWithBookCount() {
       Query<UserAndFavoritesCount> query = session.createQuery(SELECT_USERS_AND_BOOKS_COUNT_CUSTOM, UserAndFavoritesCount.class);
       List<UserAndFavoritesCount> results = query.getResultList();
       return results;
    }

    @Override
    public List<User> findAllWithAtLeastOneGenre(String genre1, String genre2, String genre3) {
        return session.createQuery(SELECT_BY_GENRE_IN, User.class)
                .setParameter("genre1", genre1)
                .setParameter("genre2", genre2)
                .setParameter("genre3", genre3)
                .getResultList();
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
    public Optional<User> findById(Integer id) {
        User u = session.find(User.class, id);
        return u != null ? Optional.of(u) : Optional.empty();
    }

    @Override
    public void delete(Integer id) {
        User u = session.getReference(User.class, id);
        session.remove(u);
    }

    @Override
    public void update(User u) {
        session.merge(u);
    }


}
