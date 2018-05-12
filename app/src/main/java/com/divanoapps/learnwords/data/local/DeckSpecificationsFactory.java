package com.divanoapps.learnwords.data.local;

/**
 * Created by dmitry on 12.05.18.
 */

public class DeckSpecificationsFactory {

    public static SQLiteQuerySpecification addedDecks() {
        return new SQLiteQuerySpecification("SELECT * FROM Deck WHERE sync = ?", Sync.ADD);
    }

    public static SQLiteQuerySpecification deletedDecks() {
        return new SQLiteQuerySpecification("SELECT * FROM Deck WHERE sync = ?", Sync.DELETE);
    }

    public static SQLiteQuerySpecification notDeletedDecks() {
        return new SQLiteQuerySpecification("SELECT * FROM Deck WHERE sync != ?", Sync.DELETE);
    }

//    public static SQLiteQuerySpecification namesOfNotDeletedDecks() {
//        return new SQLiteQuerySpecification("SELECT name FROM Deck WHERE sync != ?", Sync.DELETE);
//    }
//
//    public static SQLiteQuerySpecification names() {
//        return new SQLiteQuerySpecification("SELECT name FROM Deck");
//    }

    public static SQLiteQuerySpecification allDecks() {
        return new SQLiteQuerySpecification("SELECT * FROM Deck");
    }

    public static SQLiteQuerySpecification byName(String name) {
        return new SQLiteQuerySpecification("SELECT * FROM Deck WHERE name = ?", name);
    }

//    public static SQLiteQuerySpecification deleteByName(String name) {
//        return new SQLiteQuerySpecification("DELETE FROM Deck WHERE name = ?", name);
//    }
}
