//package com.divanoapps.learnwords.data.mappers;
//
//import android.content.Context;
//
//import com.divanoapps.learnwords.data.api2.ApiCard;
//import com.divanoapps.learnwords.data.api2.ApiCardReference;
//import com.divanoapps.learnwords.data.api2.ApiDeck;
//import com.divanoapps.learnwords.data.api2.ApiDeckInfo;
//import com.divanoapps.learnwords.data.api2.ApiExpandedDeck;
//import com.divanoapps.learnwords.data.local.StorageCard;
//import com.divanoapps.learnwords.data.local.StorageCardRepository;
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
//public class DeckMapper implements Mapper<StorageDeck, ApiDeck> {
//    private StorageDeck storageDeck;
//
//    Context context;
//
//    public DeckMapper(Context context) {
//        this.context = context;
//    }
//
//    private String getEmailOfUserWithId(Long id) {
//        StorageUser user = new StorageUserRepository(context).getById(id);
//        return user == null ? null : user.getEmail();
//    }
//
//    private ApiCardReference getApiCardReference(StorageCard storageCard) {
//        return new ApiCardReference(storageCard.getWord(), storageCard.getComment());
//    }
//
//    private ApiCard getApiCard(StorageCard storageCard) {
//        return new CardMapper(context).mapStorageToApi(storageCard);
//    }
//
//    @Override
//    public ApiDeck mapStorageToApi(StorageDeck storageDeck) {
//        ApiDeck apiDeck = new ApiDeck();
//        apiDeck.setOwner(getEmailOfUserWithId(storageDeck.getOwner()));
//        apiDeck.setName(storageDeck.getName());
//        apiDeck.setFromLanguage(storageDeck.getFromLanguage());
//        apiDeck.setToLanguage(storageDeck.getToLanguage());
//        if (storageDeck.getCards() != null) {
//            List<ApiCardReference> apiCards = new LinkedList<>();
//            for (long cardId : storageDeck.getCards()) {
//                StorageCard storageCard = new StorageCardRepository(context).getById(cardId);
//                if (storageCard != null)
//                    apiCards.add(getApiCardReference(storageCard));
//            }
//            apiDeck.setCards(apiCards);
//        }
//        return apiDeck;
//    }
//
//    public ApiExpandedDeck mapStorageToApiExpanded(StorageDeck storageDeck) {
//        ApiExpandedDeck apiExpandedDeck = new ApiExpandedDeck();
//        apiExpandedDeck.setOwner(getEmailOfUserWithId(storageDeck.getOwner()));
//        apiExpandedDeck.setName(storageDeck.getName());
//        apiExpandedDeck.setFromLanguage(storageDeck.getFromLanguage());
//        apiExpandedDeck.setToLanguage(storageDeck.getToLanguage());
//        if (storageDeck.getCards() != null) {
//            List<ApiCard> apiCards = new LinkedList<>();
//            for (long cardId : storageDeck.getCards()) {
//                StorageCard storageCard = new StorageCardRepository(context).getById(cardId);
//                if (storageCard != null)
//                    apiCards.add(getApiCard(storageCard));
//            }
//            apiExpandedDeck.setCards(apiCards);
//        }
//        return apiExpandedDeck;
//    }
//
//    public ApiDeckInfo mapStorageToApiInfo(StorageDeck storageDeck) {
//        ApiDeckInfo apiDeckInfo = new ApiDeckInfo();
//        apiDeckInfo.setOwner(getEmailOfUserWithId(storageDeck.getOwner()));
//        apiDeckInfo.setName(storageDeck.getName());
//        apiDeckInfo.setFromLanguage(storageDeck.getFromLanguage());
//        apiDeckInfo.setToLanguage(storageDeck.getToLanguage());
//        if (storageDeck.getCards() != null) {
//            int numberOfCards = 0;
//            int numberOfHidden = 0;
//            for (long cardId : storageDeck.getCards()) {
//                StorageCard storageCard = new StorageCardRepository(context).getById(cardId);
//                if (storageCard != null) {
//                    ++numberOfCards;
//                    if (storageCard.isHidden())
//                        ++numberOfHidden;
//                }
//            }
//            apiDeckInfo.setNumberOfCards(numberOfCards);
//            apiDeckInfo.setNumberOfHiddenCards(numberOfHidden);
//        }
//        else {
//            apiDeckInfo.setNumberOfCards(0);
//            apiDeckInfo.setNumberOfHiddenCards(0);
//        }
//        return apiDeckInfo;
//    }
//
//    @Override
//    public StorageDeck mapApiToStorage(ApiDeck apiDeck) {
//        StorageUser user = new StorageUserRepository(context).getByEmail(apiDeck.getOwner());
//        if (user == null)
//            return null;
//        StorageDeck storageDeck = new StorageDeckRepository(context).getByOwnerIdAndName(user.getId(), apiDeck.getName());
//        if (storageDeck == null) {
//            storageDeck = new StorageDeck();
//            storageDeck.setOwner(user.getId());
//            storageDeck.setName(apiDeck.getName());
//        }
//        else {
//            this.storageDeck = storageDeck;
//            List<Long> storageCardIds = new LinkedList<>();
//            for (ApiCardReference apiCardReference : apiDeck.getCards()) {
//                StorageCard storageCardFromApiCardReference = getStorageCardFromApiCardReference(apiCardReference);
//                if (storageCardFromApiCardReference != null) {
//                    long id = storageCardFromApiCardReference.getId();
//                    if (id != 0)
//                        storageCardIds.add(id);
//                }
//            }
//            storageDeck.setCards(storageCardIds);
//        }
//        storageDeck.setFromLanguage(apiDeck.getFromLanguage());
//        storageDeck.setToLanguage(apiDeck.getToLanguage());
//        storageDeck.setTimestamp(TimestampFactory.getTimestamp());
//        return storageDeck;
//    }
//
//    private StorageCard getStorageCardFromApiCardReference(ApiCardReference apiCardReference) {
//        return new StorageCardRepository(context).getByDeckIdAndWordAndComment(
//            storageDeck.getId(), apiCardReference.getWord(), apiCardReference.getComment());
//    }
//}
