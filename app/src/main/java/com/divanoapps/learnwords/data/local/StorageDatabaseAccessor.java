//package com.divanoapps.learnwords.data.local;
//
//import android.arch.persistence.room.Room;
//import android.content.Context;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class StorageDatabaseAccessor {
//    private static StorageDatabase storageDatabase = null;
//
//    StorageDatabaseAccessor(Context context) {
//        if (storageDatabase == null) {
//            synchronized (StorageDatabaseAccessor.class) {
//                if (storageDatabase == null)
//                    storageDatabase = Room.databaseBuilder(context, StorageDatabase.class, "learnwords").build();
//            }
//        }
//    }
//
//    public StorageDatabase getStorageDatabase() {
//        return storageDatabase;
//    }
//}
