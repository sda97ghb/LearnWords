package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT * FROM Card")
    List<Card> blockingGetAll();

    @Query("SELECT * FROM Card WHERE sync = :addSyncFlag")
    List<Card> blockingSelectAdded(Integer addSyncFlag);

    @Query("SELECT * FROM Card WHERE sync = :deleteSyncFlag")
    List<Card> blockingSelectDeleted(Integer deleteSyncFlag);

    @Query("SELECT * FROM Card WHERE sync != :deleteSyncFlag")
    List<Card> blockingSelectNotDeleted(Integer deleteSyncFlag);

    @Query("SELECT * FROM Card WHERE sync != :deleteSyncFlag")
    Single<List<Card>> selectNotDeleted(Integer deleteSyncFlag);

    @Query("SELECT * FROM Card WHERE deckName = :deckName AND word = :word AND comment = :comment")
    Single<Card> find(String deckName, String word, String comment);

    @Query("SELECT * FROM Card WHERE deckName = :deckName AND word = :word AND comment = :comment")
    Card blockingFind(String deckName, String word, String comment);

    @Query("SELECT * FROM Card WHERE deckName=:deckName")
    Single<List<Card>> findAllCardsFromDeckWithName(String deckName);

    @Query("SELECT * FROM Card WHERE deckName=:deckName")
    List<Card> blockingFindAllCardsFromDeckWithName(String deckName);

    @Insert(onConflict = REPLACE)
    long blockingInsert(Card card);

    @Update
    int blockingUpdate(Card card);

    @Query("DELETE FROM Card WHERE deckName = :deckName AND word = :word AND comment = :comment")
    int blockingDelete(String deckName, String word, String comment);

    @Delete
    int blockingDelete(Card card);

    @Delete
    int blockingDelete(Card... card);
}
