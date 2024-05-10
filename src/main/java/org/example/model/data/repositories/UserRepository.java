package org.example.model.data.repositories;

import java.util.List;

import org.example.model.User;

public interface UserRepository extends Repository<User>{

    List<User> findAllUsersForFavoritesBooks(int id);
    List<User> findAllUsersForAtLeastOneGenre(String genre);
}
