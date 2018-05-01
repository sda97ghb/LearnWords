//package com.divanoapps.learnwords.data;
//
///**
// * Created by dmitry on 29.04.18.
// */
//
////public class LocalLearnWordsService implements Api {
////
////    private Context context;
////    private String email;
////
////    public LocalLearnWordsService(Context context) {
////        this.context = context;
////        this.email = "unauthorized";
////    }
////
////    public LocalLearnWordsService(Context context, String email) {
////        this.context = context;
////        this.email = email;
////    }
////
////    private String getEmail() {
////        return email;
////    }
////
////    private StorageUserRepository getStorageUserRepository() {
////        return new StorageUserRepository(context);
////    }
////
////    private StorageDeckRepository getStorageDeckRepository() {
////        return new StorageDeckRepository(context);
////    }
////
////    private StorageCardRepository getStorageCardRepository() {
////        return new StorageCardRepository(context);
////    }
////
////    private UserMapper getUserMapper() {
////        return new UserMapper(context);
////    }
////
////    private DeckMapper getDeckMapper() {
////        return new DeckMapper(context);
////    }
////
////    private CardMapper getCardMapper() {
////        return new CardMapper(context);
////    }
////
////    @Override
////    public Single<String> testIdToken(String idToken) {
////        return null;
////    }
////
////    @Override
////    public Single<String> testRotate() {
////        return null;
////    }
////
////    @Override
////    public Completable registerUser() {
////        return Completable.create(emitter -> {
////            StorageUser storageUser = getStorageUserRepository().getByEmail(getEmail());
////            if (storageUser == null) {
////                storageUser = new StorageUser();
////                storageUser.setEmail(getEmail());
////                storageUser.setTimestamp(TimestampFactory.getTimestamp());
////                getStorageUserRepository().insert(storageUser);
////            }
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Single<ApiUser> getUser() {
////        return Single.create((SingleOnSubscribe<ApiUser>) emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null)
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////            else
////                emitter.onSuccess(getUserMapper().mapStorageToApi(user));
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Single<ApiExpandedUser> getExpandedUser() {
////        return Single.create((SingleOnSubscribe<ApiExpandedUser>) emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null)
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////            else
////                emitter.onSuccess(getUserMapper().mapStorageToApiExpanded(user));
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Single<ApiDeck> getDeck(String deckName) {
////        return Single.create((SingleOnSubscribe<ApiDeck>) emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deckName);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with this name."));
////                return;
////            }
////
////            emitter.onSuccess(getDeckMapper().mapStorageToApi(deck));
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Single<ApiExpandedDeck> getExpandedDeck(String deckName) {
////        return Single.create((SingleOnSubscribe<ApiExpandedDeck>) emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deckName);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with this name."));
////                return;
////            }
////
////            emitter.onSuccess(getDeckMapper().mapStorageToApiExpanded(deck));
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable saveDeck(ApiDeck deck) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            deck.setOwner(email);
////            StorageDeck storageDeck = getDeckMapper().mapApiToStorage(deck);
////
////            StorageDeck storedDeck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deck.getName());
////            if (storedDeck == null) {
////                getStorageDeckRepository().insert(storageDeck);
////
////                storedDeck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deck.getName());
////                if (storedDeck == null) {
////                    emitter.onError(new ApiError(ApiError.METHOD, 2, "Unable to store the deck."));
////                    return;
////                }
////
////                user.getPersonalDecks().add(storedDeck.getId());
////                getStorageUserRepository().replaceWithId(user.getId(), user);
////            }
////            else {
////                storageDeck.setId(storedDeck.getId());
////                getStorageDeckRepository().replaceWithId(storageDeck.getId(), storageDeck);
////            }
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable deleteDeck(String name) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), name);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with this name."));
////                return;
////            }
////
////            user.getPersonalDecks().remove(deck.getId());
////            getStorageUserRepository().replaceWithId(user.getId(), user);
////
////            for (long cardId : deck.getCards())
////                getStorageCardRepository().deleteWithId(cardId);
////
////            getStorageDeckRepository().deleteWithId(deck.getId());
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable updateDeck(String name, Map<String, Object> properties) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), name);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with this name."));
////                return;
////            }
////
////            for (Map.Entry<String, Object> property : properties.entrySet()) {
////                switch (property.getKey()) {
////                    case "name":
////                        deck.setName((String) property.getValue());
////                        break;
////                    case "fromLanguage":
////                        deck.setFromLanguage((String) property.getValue());
////                        break;
////                    case "toLanguage":
////                        deck.setToLanguage((String) property.getValue());
////                        break;
////                    default:
////                        emitter.onError(new ApiError(ApiError.METHOD, 3, "Unknown property: " + property));
////                        return;
////                }
////            }
////
////            getStorageDeckRepository().replaceWithId(deck.getId(), deck);
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable saveCard(ApiCard card) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), card.getDeck());
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with name " + card.getDeck() + "."));
////                return;
////            }
////
////            card.setOwner(email);
////            StorageCard newCard = getCardMapper().mapApiToStorage(card);
////
////            StorageCard oldCard = getStorageCardRepository().getByDeckIdAndWordAndComment(deck.getId(), card.getWord(), card.getComment());
////            if (oldCard == null) {
////                getStorageCardRepository().insert(newCard);
////
////                newCard = getStorageCardRepository().getByDeckIdAndWordAndComment(deck.getId(), card.getWord(), card.getComment());
////                if (newCard == null) {
////                    emitter.onError(new ApiError(ApiError.METHOD, 3, "Unable to store the card."));
////                }
////
////                deck.getCards().add(newCard.getId());
////                getStorageDeckRepository().replaceWithId(deck.getId(), deck);
////            }
////            else {
////                newCard.setId(oldCard.getId());
////                getStorageCardRepository().replaceWithId(newCard.getId(), newCard);
////            }
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Single<ApiCard> getCard(String deckName, String word, String comment) {
////        return Single.create((SingleOnSubscribe<ApiCard>) emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deckName);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with name " + deckName + "."));
////                return;
////            }
////
////            StorageCard card = getStorageCardRepository().getByDeckIdAndWordAndComment(deck.getId(), word, comment);
////            if (card == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 3, deckName + " does not contain the card with word " + word + " and comment " + comment + "."));
////                return;
////            }
////
////            emitter.onSuccess(getCardMapper().mapStorageToApi(card));
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable deleteCard(String deckName, String word, String comment) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deckName);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with name " + deckName + "."));
////                return;
////            }
////
////            StorageCard card = getStorageCardRepository().getByDeckIdAndWordAndComment(deck.getId(), word, comment);
////            if (card == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 3, deckName + " does not have any card with word " + word + " and comment " + comment + "."));
////                return;
////            }
////
////            getStorageCardRepository().deleteWithId(card.getId());
////
////            deck.getCards().remove(card.getId());
////            getStorageDeckRepository().replaceWithId(deck.getId(), deck);
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////
////    @Override
////    public Completable updateCard(String deckName, String word, String comment, Map<String, Object> properties) {
////        return Completable.create(emitter -> {
////            String email = getEmail();
////
////            StorageUser user = getStorageUserRepository().getByEmail(email);
////            if (user == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 1, "There is no user with this email."));
////                return;
////            }
////
////            StorageDeck deck = getStorageDeckRepository().getByOwnerIdAndName(user.getId(), deckName);
////            if (deck == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 2, "There is no deck with name " + deckName + "."));
////                return;
////            }
////
////            StorageCard card = getStorageCardRepository().getByDeckIdAndWordAndComment(deck.getId(), word, comment);
////            if (card == null) {
////                emitter.onError(new ApiError(ApiError.METHOD, 3, deckName + " does not contain the card with word " + word + " and comment " + comment + "."));
////                return;
////            }
////
////            for (Map.Entry<String, Object> property : properties.entrySet()) {
////                switch (property.getKey()) {
////                    case "word":
////                        card.setWord((String) property.getValue());
////                        break;
////                    case "comment":
////                        card.setComment((String) property.getValue());
////                        break;
////                    case "translation":
////                        card.setTranslation((String) property.getValue());
////                        break;
////                    case "difficulty":
////                        card.setDifficulty(((Number) property.getValue()).intValue());
////                        break;
////                    case "hidden":
////                        card.setHidden((boolean) property.getValue());
////                        break;
////                    default:
////                        emitter.onError(new ApiError(ApiError.METHOD, 4, "Unknown property: " + property));
////                        return;
////                }
////            }
////
////            getStorageCardRepository().replaceWithId(card.getId(), card);
////
////            emitter.onComplete();
////        })
////            .subscribeOn(Schedulers.newThread())
////            .observeOn(AndroidSchedulers.mainThread());
////    }
////}
