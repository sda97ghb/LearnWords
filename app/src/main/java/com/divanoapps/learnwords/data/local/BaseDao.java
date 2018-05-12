package com.divanoapps.learnwords.data.local;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by dmitry on 12.05.18.
 */

interface BaseDao<T> {
    @RawQuery
    List<T> query(SupportSQLiteQuery supportSQLiteQuery);

    @Insert
    void insert(T... items);

    @Update
    void update(T item);

    @Delete
    void delete(T... items);
}
