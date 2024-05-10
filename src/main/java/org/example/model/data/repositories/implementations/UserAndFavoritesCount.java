package org.example.model.data.repositories.implementations;

import org.example.model.User;

public class UserAndFavoritesCount {
    private User user;
    private int favoriteCount;
    

    public UserAndFavoritesCount(User user, int favoriteCount) {
        this.user = user;
        this.favoriteCount = favoriteCount;
    }

    public static  UserAndFavoritesCount fromObjectArray(Object[] values){
        return new UserAndFavoritesCount((User)values[0],(Integer)values[1]);
    }

    public User getUser() {
        return user;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    
}