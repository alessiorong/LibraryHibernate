package org.example.model.data.repositories.abstractions;

import org.example.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findAllByAuthor(int authorId);
    List<User> findAllByAtLeastOneGenre(String genre);
    List<User> findAllWithOneGenre(String genre);
    List<Object[]> findAllWithBookCount();
    List<User> findAllWithAtLeastOneGenre(String genre1, String genre2, String genre3);
}
