package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by dmitry on 29.04.18.
 */

@Dao
public interface DeckDao extends BaseDao<Deck> {
//    @Query("SELECT * FROM Deck WHERE sync = :addSyncFlag")
//    List<Deck> blockingSelectAdded(Integer addSyncFlag);
//
//    @Query("SELECT * FROM Deck WHERE sync = :deleteSyncFlag")
//    List<Deck> blockingSelectDeleted(Integer deleteSyncFlag);
//
//    @Query("SELECT * FROM Deck WHERE sync != :deleteSyncFlag")
//    List<Deck> blockingSelectNotDeleted(Integer deleteSyncFlag);
//
//    @Query("SELECT * FROM Deck WHERE sync != :deleteSyncFlag")
//    Single<List<Deck>> selectNotDeleted(Integer deleteSyncFlag);
//
//    @Query("SELECT name FROM Deck WHERE sync != :deleteSyncFlag")
//    Single<List<String>> getNamesOfNotDeleted(Integer deleteSyncFlag);
//
//    @Query("SELECT name FROM Deck")
//    List<String> blockingGetNames();
//
//    @Query("SELECT * FROM Deck")
//    Single<List<Deck>> getAll();
//
//    @Query("SELECT * FROM Deck")
//    List<Deck> blockingGetAll();
//
//    @Query("SELECT * FROM Deck WHERE name = :name")
//    Single<Deck> find(String name);
//
//    @RawQuery
//    Deck blockingFind(SupportSQLiteQuery query);
//
//    @Query("SELECT * FROM Deck WHERE name = :name")
//    Deck blockingFind(String name);
//
//    @Insert(onConflict = REPLACE)
//    long blockingInsert(Deck deck);
//
//    @Update
//    int blockingUpdate(Deck deck);
//
//    @Query("DELETE FROM Deck WHERE name = :name")
//    int blockingDelete(String name);
//
//    @Delete
//    int blockingDelete(Deck deck);
//
//    @Delete
//    int blockingDelete(Deck... decks);
}
