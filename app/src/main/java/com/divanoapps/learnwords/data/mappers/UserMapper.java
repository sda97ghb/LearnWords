//package com.divanoapps.learnwords.data.mappers;
//
//import android.content.Context;
//
//import com.divanoapps.learnwords.data.api2.ApiDeckInfo;
//import com.divanoapps.learnwords.data.api2.ApiExpandedUser;
//import com.divanoapps.learnwords.data.api2.ApiSharedDeckReference;
//import com.divanoapps.learnwords.data.api2.ApiUser;
//import com.divanoapps.learnwords.data.local.StorageDeck;
//import com.divanoapps.learnwords.data.local.StorageDeckRepository;
//import com.divanoapps.learnwords.data.local.TimestampFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class UserMapper implements Mapper<StorageUser, ApiUser> {
//    private StorageUser storageUser;
//
//    private Context context;
//
//    public UserMapper(Context context) {
//        this.context = context;
//    }
//
//    private ApiSharedDeckReference getApiSharedDeckReferenceFromStorageDeck(StorageDeck storageDeck) {
//        return new ApiSharedDeckReference(storageUser.getEmail(), storageDeck.getName());
//    }
//
//    private Long getDeckIdFromName(String name) {
//        StorageDeck deck = new StorageDeckRepository(context).getByOwnerIdAndName(storageUser.getId(), name);
//        return deck == null ? null : deck.getId();
//    }
//
//    private Long getDeckIdFromApiSharedDeckReference(ApiSharedDeckReference apiSharedDeckReference) {
//        StorageUser user = new StorageUserRepository(context).getByEmail(apiSharedDeckReference.getEmail());
//        if (user == null)
//            return null;
//        StorageDeck deck = new StorageDeckRepository(context).getByOwnerIdAndName(user.getId(), apiSharedDeckReference.getName());
//        return deck == null ? null : deck.getId();
//    }
//
//    @Override
//    public ApiUser mapStorageToApi(StorageUser storageUser) {
//        this.storageUser = storageUser;
//
//        ApiUser apiUser = new ApiUser();
//        apiUser.setEmail(storageUser.getEmail());
//        List<String> deckNames = new LinkedList<>();
//        for (long deckId: storageUser.getPersonalDecks()) {
//            StorageDeck storageDeck = new StorageDeckRepository(context).getById(deckId);
//            if (storageDeck != null) {
//                deckNames.add(storageDeck.getName());
//            }
//        }
//        apiUser.setPersonalDecks(deckNames);
//        return apiUser;
//    }
//
//    public ApiExpandedUser mapStorageToApiExpanded(StorageUser storageUser) {
//        this.storageUser = storageUser;
//
//        DeckMapper deckMapper = new DeckMapper(context);
//
//        ApiExpandedUser apiUser = new ApiExpandedUser();
//        apiUser.setEmail(storageUser.getEmail());
//        List<ApiDeckInfo> apiDeckInfos = new LinkedList<>();
//        for (long deckId: storageUser.getPersonalDecks()) {
//            StorageDeck storageDeck = new StorageDeckRepository(context).getById(deckId);
//            if (storageDeck != null) {
//                apiDeckInfos.add(deckMapper.mapStorageToApiInfo(storageDeck));
//            }
//        }
//        apiUser.setPersonalDecks(apiDeckInfos);
//        return apiUser;
//    }
//
//    @Override
//    public StorageUser mapApiToStorage(ApiUser apiUser) {
//        StorageUser storageUser = new StorageUserRepository(context).getByEmail(apiUser.getEmail());
//        if (storageUser == null) {
//            storageUser = new StorageUser();
//            storageUser.setEmail(apiUser.getEmail());
//        }
//        else {
//            this.storageUser = storageUser;
//            List<Long> deckIds = new LinkedList<>();
//            for (String deckName: apiUser.getPersonalDecks()) {
//                Long deckIdFromName = getDeckIdFromName(deckName);
//                if (deckIdFromName != null)
//                    deckIds.add(deckIdFromName);
//            }
//            storageUser.setPersonalDecks(deckIds);
//        }
//        storageUser.setTimestamp(TimestampFactory.getTimestamp());
//
//        return storageUser;
//    }
//}
