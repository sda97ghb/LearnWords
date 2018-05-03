package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by dmitry on 29.04.18.
 */

@Dao
public interface DeckDao {
    @Query("SELECT name FROM Deck")
    Single<List<String>> getNames();

    @Query("SELECT * FROM Deck")
    Single<List<Deck>> getAll();

    @Query("SELECT * FROM Deck WHERE name = :name")
    Single<Deck> find(String name);

    @Insert(onConflict = REPLACE)
    long insert(Deck deck);

    @Query("DELETE FROM Deck WHERE name = :name")
    int delete(String name);

    @Delete
    int delete(Deck deck);

    @Delete
    int delete(Deck... decks);
}
