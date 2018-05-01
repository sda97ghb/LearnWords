//package com.divanoapps.learnwords.data.mappers;
//
//import android.content.Context;
//
//import com.divanoapps.learnwords.data.api2.ApiCard;
//import com.divanoapps.learnwords.data.local.StorageCard;
//import com.divanoapps.learnwords.data.local.StorageCardRepository;
//import com.divanoapps.learnwords.data.local.StorageDeck;
//import com.divanoapps.learnwords.data.local.StorageDeckRepository;
//import com.divanoapps.learnwords.data.local.TimestampFactory;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
//public class CardMapper implements Mapper<StorageCard, ApiCard> {
//
//    Context context;
//
//    public CardMapper(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public ApiCard mapStorageToApi(StorageCard storageCard) {
//        StorageDeck deck = new StorageDeckRepository(context).getById(storageCard.getDeckId());
//        if (deck == null)
//            return null;
//
//        StorageUser user = new StorageUserRepository(context).getById(deck.getOwner());
//        if (user == null)
//            return null;
//
//        ApiCard apiCard = new ApiCard();
//        apiCard.setOwner(user.getEmail());
//        apiCard.setDeck(deck.getName());
//        apiCard.setWord(storageCard.getWord());
//        apiCard.setComment(storageCard.getComment());
//        apiCard.setTranslation(storageCard.getTranslation());
//        apiCard.setDifficulty(storageCard.getDifficulty());
//        apiCard.setHidden(storageCard.isHidden());
//        return apiCard;
//    }
//
//    @Override
//    public StorageCard mapApiToStorage(ApiCard apiCard) {
//        StorageUser user = new StorageUserRepository(context).getByEmail(apiCard.getOwner());
//        if (user == null)
//            return null;
//        StorageDeck deck = new StorageDeckRepository(context).getByOwnerIdAndName(user.getId(), apiCard.getDeck());
//        if (deck == null)
//            return null;
//        StorageCard storageCard = new StorageCardRepository(context)
//            .getByDeckIdAndWordAndComment(deck.getId(), apiCard.getWord(), apiCard.getComment());
//        if (storageCard == null) {
//            storageCard = new StorageCard();
//            storageCard.setDeckId(deck.getId());
//            storageCard.setWord(apiCard.getWord());
//            storageCard.setComment(apiCard.getComment());
//        }
//        storageCard.setTranslation(apiCard.getTranslation());
//        storageCard.setDifficulty(apiCard.getDifficulty());
//        storageCard.setHidden(apiCard.isHidden());
//        storageCard.setTimestamp(TimestampFactory.getTimestamp());
//        return storageCard;
//    }
//}
