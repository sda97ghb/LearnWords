package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.RoomDatabase;

/**
 * Created by dmitry on 29.04.18.
 */

@android.arch.persistence.room.Database(entities = { Deck.class, Card.class }, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();
}
