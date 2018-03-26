package com.divanoapps.learnwords.data.api2;

import java.util.Map;

import retrofit2.Call;

public interface Api2 {

    @ApiRequest(entity = "user", method = "add")
    Call<ApiUser> addUser(@ApiParameter("email") String email, @ApiParameter("password") String password);

    @ApiRequest(entity = "user", method = "get")
    Call<Void> getUser(@ApiParameter("email") String email);

    @ApiRequest(entity = "deck", method = "get")
    Call<ApiDeck> getDeck(@ApiParameter("email") String email, @ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "getExpanded")
    Call<ApiExpandedDeck> getExpandedDeck(@ApiParameter("email") String email, @ApiParameter("name") String deckName);

    @ApiRequest(entity = "deck", method = "save")
    Call<Void> saveDeck(@ApiParameter("email") String email, @ApiParameter("deck") ApiDeck deck);

    @ApiRequest(entity = "deck", method = "delete")
    Call<Void> deleteDeck(@ApiParameter("email") String email, @ApiParameter("name") String name);

    @ApiRequest(entity = "deck", method = "update")
    Call<Void> updateDeck(@ApiParameter("email") String email, @ApiParameter("name") String name,
                            @ApiParameter("properties") Map<String, Object> properties);

    @ApiRequest(entity = "card", method = "save")
    Call<Void> saveCard(@ApiParameter("email") String email, @ApiParameter("card") ApiCard card);

    @ApiRequest(entity = "card", method = "get")
    Call<ApiCard> getCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "delete")
    Call<Void> deleteCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment);

    @ApiRequest(entity = "card", method = "update")
    Call<Void> updateCard(@ApiParameter("email") String email, @ApiParameter("deck") String deckName,
                            @ApiParameter("word") String word, @ApiParameter("comment") String comment,
                            @ApiParameter("properties") Map<String, Object> properties);
}
