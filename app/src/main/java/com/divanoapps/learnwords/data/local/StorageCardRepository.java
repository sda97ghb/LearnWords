//package com.divanoapps.learnwords.data.local;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class StorageCardRepository {
////    private StorageDatabase database;
////
////    public StorageCardRepository(Context context) {
////        this.database = new StorageDatabaseAccessor(context).getStorageDatabase();
////    }
////
////    public List<StorageCard> getAllCards() {
////        return database.storageCardDao().getAll();
////    }
////
////    public StorageCard getById(long id) {
////        return database.storageCardDao().getById(id);
////    }
////
////    public StorageCard getByDeckIdAndWordAndComment(long deckId, String word, String comment) {
////        return database.storageCardDao().getByDeckIdAndWordAndComment(deckId, word, comment);
////    }
////
////    public void insert(StorageCard storageCard) {
////        database.storageCardDao().insert(storageCard);
////    }
////
////    public void replaceWithId(long id, StorageCard storageCard) {
////        database.storageCardDao().deleteWithId(id);
////        database.storageCardDao().insert(storageCard);
////    }
////
////    public void deleteWithId(long id) {
////        database.storageCardDao().deleteWithId(id);
////    }
//}
