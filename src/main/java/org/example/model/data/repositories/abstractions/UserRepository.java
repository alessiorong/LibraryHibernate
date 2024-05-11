package org.example.model.data.repositories.abstractions;

import org.example.model.User;

import java.util.List;

public interface UserRepository extends Repository<User>{
        List<User> findAllByAuthorId(int id);
        List<User> findAllWithAtLeastOneBookByGenre(String genre);
        List<User> findAllWithAllBooksByGenre(String genre);
        List<Object[]> getUsersWithBookCount();
        List<User> findAllWithAtLeastOneBookByGenre(String genre,String genre1, String genre2);

}
