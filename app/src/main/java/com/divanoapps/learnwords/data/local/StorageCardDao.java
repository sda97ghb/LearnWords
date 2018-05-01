//package com.divanoapps.learnwords.data.local;
//
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//
//import java.util.List;
//
//import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//@Dao
//public interface StorageCardDao {
//    @Query("SELECT * FROM StorageCard")
//    List<StorageCard> getAll();
//
//    @Query("SELECT * FROM StorageCard WHERE deckName = :deckName AND word = :word AND comment = :comment")
//    StorageCard find(String deckName, String word, String comment);
//
//    @Query("SELECT * FROM StorageCard WHERE deckName=:deckName")
//    List<StorageCard> findAllCardsFromDeckWithName(String deckName);
//
//    @Insert(onConflict = REPLACE)
//    void insert(StorageCard storageCard);
//
//    @Query("DELETE FROM StorageCard WHERE deckName = :deckName AND word = :word AND comment = :comment")
//    void delete(String deckName, String word, String comment);
//
//    @Delete
//    void delete(StorageCard storageCard);
//}
