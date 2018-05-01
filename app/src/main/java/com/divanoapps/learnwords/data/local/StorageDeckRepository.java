//package com.divanoapps.learnwords.data.local;
//
//import android.content.Context;
//
//import java.util.List;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class StorageDeckRepository {
////    private StorageDatabase database;
////
////    public StorageDeckRepository(Context context) {
////        this.database = new StorageDatabaseAccessor(context).getStorageDatabase();
////    }
////
////    public List<StorageDeck> getAllDecks() {
////        List<StorageDeck> allDecks = database.storageDeckDao().getAllDecks();
////        for (StorageDeck storageDeck : allDecks)
////            if (storageDeck != null)
////                storageDeck.setCards(database.storageCardDao().findAllCardsFromDeckWithId(storageDeck.getId()));
////        return allDecks;
////    }
////
////    public StorageDeck getById(long id) {
////        StorageDeck storageDeck = database.storageDeckDao().getById(id);
////        if (storageDeck != null)
////            storageDeck.setCards(database.storageCardDao().findAllCardsFromDeckWithId(id));
////        return storageDeck;
////    }
////
////    public StorageDeck getByOwnerIdAndName(long ownerId, String name) {
////        StorageDeck storageDeck = database.storageDeckDao().getByOwnerIdAndName(ownerId, name);
////        if (storageDeck != null)
////            storageDeck.setCards(database.storageCardDao().findAllCardsFromDeckWithId(storageDeck.getId()));
////        return storageDeck;
////    }
////
////    public void insert(StorageDeck storageDeck) {
////        database.storageDeckDao().insert(storageDeck);
////    }
////
////    public void replaceWithId(long deckId, StorageDeck storageDeck) {
////        database.storageDeckDao().deleteWithId(deckId);
////        database.storageDeckDao().insert(storageDeck);
////    }
////
////    public void deleteWithId(long id) {
////        database.storageDeckDao().deleteWithId(id);
////    }
//}
