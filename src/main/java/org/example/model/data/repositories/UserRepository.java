package org.example.model.data.repositories;

import org.example.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAllByAuthor(int authorId);
    List<User> findAllByAtLeastOneGenre(String genre);
    List<User> findAllWithOnePreference(String genre);
    Object[] findAllWithBookCount();
    List<User> findAllWithOnePreference(String genre1, String genre2, String genre3);
}
//1)
//Implementare gli Unit Test per le funzioni scritte
//2)
//Creare User Repository e inserirci una query che ritrovi tutti gli utenti che hanno tra i loro favoriti almeno un libro scritto da un autore di cui io do l'Id in input
//3)
//Aggiungere una variabile (String) che indica il genere del libro
//Dare tutti gli utenti che hanno tra i loro favoriti almeno un libro appartenente a una categoria che do in input
//
//Dare tutti gli utenti per cui i loro favoriti sono tutti appartenenti alla categoria in input
//4)
//Una query che ritorni tutti gli utenti accompagnati dal conteggio dei loro libri favoriti ordinati per numero di favoriti DESC
//5)
//Dare tutti gli utenti che hanno almeno uno dei favoriti in una sequenza di tre categorie che do in input
//
//Fare un test per ogni query
//