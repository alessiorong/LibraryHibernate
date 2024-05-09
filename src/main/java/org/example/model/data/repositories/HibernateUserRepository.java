package org.example.model.data.repositories;

import org.example.model.User;
import org.hibernate.Session;

import java.util.List;

public class HibernateUserRepository implements UserRepository {

    private Session session;
    private static final String SELECT_BY_AUTHOR_ID = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b WHERE b.author.id = :authorId
            """;
    private static final String SELECT_BY_AT_LEAST_ONE_GENRE = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b WHERE b.genre = :genre
            """;
    private static final String SELECT_BY_SINGLE_GENRE = """
            SELECT u FROM User u 
            WHERE (SELECT COUNT(b) FROM u.favouriteBooks as b
            WHERE b.genre = :genre) = 
            (SELECT COUNT(b) FROM u.favouriteBooks as b) 
            """;
    private static final String SELECT_USERS_AND_THEIR_BOOKS_COUNT = """
            SELECT u, COUNT(u.favouriteBooks) c FROM User u JOIN u.favouriteBooks b
            GROUP BY u ORDER BY c DESC
            """;
    private static final String SELECT_BY_GENRE_IN = """
            SELECT DISTINCT u FROM User u JOIN u.favouriteBooks b 
            WHERE b.genre IN (:genre1, :genre2, :genre3)
            """;
    private static final String SELECT_BY_GENRE_IN_V2 = """
            SELECT DISTINCT u FROM User u
            WHERE EXISTS(
                SELECT 1 FROM u.favouriteBooks as b
                WHERE b.genre IN (:genre1, :genre2, :genre3)
            """;

    public HibernateUserRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<User> findAllByAuthor(int authorId) {
        return session.createQuery(SELECT_BY_AUTHOR_ID, User.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
    }

    @Override
    public List<User> findAllByAtLeastOneGenre(String genre) {
        return session.createQuery(SELECT_BY_AT_LEAST_ONE_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<User> findAllWithOnePreference(String genre) {
        return session.createQuery(SELECT_BY_SINGLE_GENRE, User.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public Object[] findAllWithBookCount() {
        return session.createQuery(SELECT_USERS_AND_THEIR_BOOKS_COUNT, Object[].class)
                .uniqueResult();
    }

    @Override
    public List<User> findAllWithOnePreference(String genre1, String genre2, String genre3) {
        return session.createQuery(SELECT_BY_GENRE_IN, User.class)
                .setParameter("genre1", genre1)
                .setParameter("genre2", genre2)
                .setParameter("genre3", genre3)
                .getResultList();
    }
}
