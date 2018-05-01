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
//public interface StorageDeckDao {
//    @Query("SELECT * FROM StorageDeck")
//    List<StorageDeck> getAll();
//
//    @Query("SELECT * FROM StorageDeck WHERE name = :name")
//    StorageDeck find(String name);
//
//    @Insert(onConflict = REPLACE)
//    void insert(StorageDeck storageDeck);
//
//    @Query("DELETE FROM StorageDeck WHERE name = :name")
//    void delete(String name);
//
//    @Delete
//    void delete(StorageDeck storageDeck);
//}
