package com.divanoapps.learnwords.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by dmitry on 29.04.18.
 */

public class DatabaseAccessor {
    private static Database database = null;

    public DatabaseAccessor(Context context) {
        if (database == null) {
            synchronized (DatabaseAccessor.class) {
                if (database == null)
                    database = Room.databaseBuilder(context, Database.class, "learnwords").build();
            }
        }
    }

    public Database getStorageDatabase() {
        return database;
    }
}
