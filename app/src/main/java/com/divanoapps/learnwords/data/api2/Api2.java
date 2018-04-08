package com.divanoapps.learnwords.data.api2;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface Api2 {

    @ApiRequest(entity = "user", method = "add")
    Completable addUser(@ApiParameter("email") String email, @ApiParameter("password") String password);

    @ApiRequest(entity = "user", method = "get")
    Single<ApiUser> getUser(@ApiParameter("email") String email);

    @ApiRequest(entity = "user", method = "getExpanded")
    Single<ApiExpandedUser> getExpandedUser(@ApiParameter("email") String email);

    @ApiRequest(entity = "deck", method = "get")
    Single<ApiDeck> getDeck(@ApiParameter("email") String email, @ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "getExpanded")
    Single<ApiExpandedDeck> getExpandedDeck(@ApiParameter("email") String email, @ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "save")
    Completable saveDeck(@ApiParameter("email") String email, @ApiParameter("deck") ApiDeck deck);

    @ApiRequest(entity = "deck", method = "delete")
    Completable deleteDeck(@ApiParameter("email") String email, @ApiParameter("name") String name);

    @ApiRequest(entity = "deck", method = "update")
    Completable updateDeck(@ApiParameter("email") String email, @ApiParameter("name") String name,
                            @ApiParameter("properties") Map<String, Object> properties);

    @ApiRequest(entity = "card", method = "save")
    Completable saveCard(@ApiParameter("email") String email, @ApiParameter("card") ApiCard card);

    @ApiRequest(entity = "card", method = "get")
    Single<ApiCard> getCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "delete")
    Completable deleteCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "update")
    Completable updateCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment,
                            @ApiParameter("properties") Map<String, Object> properties);
}