package com.divanoapps.learnwords.data.local;

/**
 * Created by dmitry on 12.05.18.
 */

public class CardSpecificationsFactory {

    public static SQLiteQuerySpecification allCards() {
        return new SQLiteQuerySpecification("SELECT * FROM Card");
    }

    public static SQLiteQuerySpecification addedCards() {
        return new SQLiteQuerySpecification("SELECT * FROM Card WHERE sync = ?", Sync.ADD);
    }

    public static SQLiteQuerySpecification deletedCards() {
        return new SQLiteQuerySpecification("SELECT * FROM Card WHERE sync = ?", Sync.DELETE);
    }

    public static SQLiteQuerySpecification notDeletedCards() {
        return new SQLiteQuerySpecification("SELECT * FROM Card WHERE sync IS NULL OR sync != ?", Sync.DELETE);
    }

    public static SQLiteQuerySpecification byDeckNameAndWordAndComment(String deckName, String word, String comment) {
        return new SQLiteQuerySpecification("SELECT * FROM Card WHERE deckName = ? AND word = ? AND comment = ?",
            deckName, word, comment);
    }

    public static SQLiteQuerySpecification cardsWithDeckName(String deckName) {
        return new SQLiteQuerySpecification("SELECT * FROM Card WHERE deckName = ?", deckName);
    }

//    public static SQLiteQuerySpecification deleteByDeckNameAndWordAndComment(String deckName, String word, String comment) {
//        return new SQLiteQuerySpecification("DELETE FROM Card WHERE deckName = ? AND word = ? AND comment = ?",
//            deckName, word, comment);
//    }
}
