package com.divanoapps.learnwords.data.api2;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface Api {

    // Test methods

    @ApiRequest(entity = "test", method = "idToken")
    Single<String> testIdToken(@ApiParameter("id_token") String idToken);

    @ApiRequest(entity = "test", method = "rotate")
    Single<String> testRotate();

    // Actual api methods

    @ApiRequest(entity = "user", method = "register")
    @ApiRequireAuthorization
    Completable registerUser();

    @ApiRequest(entity = "user", method = "get")
    @ApiRequireAuthorization
    Single<ApiUser> getUser();

    @ApiRequest(entity = "user", method = "getExpanded")
    @ApiRequireAuthorization
    Single<ApiExpandedUser> getExpandedUser();

    @ApiRequest(entity = "deck", method = "get")
    @ApiRequireAuthorization
    Single<ApiDeck> getDeck(@ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "getExpanded")
    @ApiRequireAuthorization
    Single<ApiExpandedDeck> getExpandedDeck(@ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "save")
    @ApiRequireAuthorization
    Completable saveDeck(@ApiParameter("deck") ApiDeck deck);

    @ApiRequest(entity = "deck", method = "delete")
    @ApiRequireAuthorization
    Completable deleteDeck(@ApiParameter("name") String name);

    @ApiRequest(entity = "deck", method = "update")
    @ApiRequireAuthorization
    Completable updateDeck(@ApiParameter("name") String name,
                           @ApiParameter("properties") Map<String, Object> properties);

    @ApiRequest(entity = "card", method = "save")
    @ApiRequireAuthorization
    Completable saveCard(@ApiParameter("card") ApiCard card);

    @ApiRequest(entity = "card", method = "get")
    @ApiRequireAuthorization
    Single<ApiCard> getCard(@ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "delete")
    @ApiRequireAuthorization
    Completable deleteCard(@ApiParameter("deck") String deckName,
                           @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "update")
    @ApiRequireAuthorization
    Completable updateCard(@ApiParameter("deck") String deckName,
                           @ApiParameter("word") String word, @ApiParameter("comment") String comment,
                           @ApiParameter("properties") Map<String, Object> properties);
}
