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
public interface CardDao {
    @Query("SELECT * FROM Card")
    Single<List<Card>> getAll();

    @Query("SELECT * FROM Card WHERE deckName = :deckName AND word = :word AND comment = :comment")
    Single<Card> find(String deckName, String word, String comment);

    @Query("SELECT * FROM Card WHERE deckName=:deckName")
    Single<List<Card>> findAllCardsFromDeckWithName(String deckName);

    @Query("SELECT * FROM Card WHERE deckName=:deckName")
    List<Card> findAllCardsFromDeckWithNameBlocking(String deckName);

    @Insert(onConflict = REPLACE)
    long insert(Card card);

    @Query("DELETE FROM Card WHERE deckName = :deckName AND word = :word AND comment = :comment")
    int delete(String deckName, String word, String comment);

    @Delete
    int delete(Card card);
}
