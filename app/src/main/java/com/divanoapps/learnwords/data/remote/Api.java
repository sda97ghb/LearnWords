package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.dogfish.ApiParameter;
import com.divanoapps.learnwords.data.dogfish.ApiRequest;
import com.divanoapps.learnwords.data.dogfish.ApiRequireAuthorization;

import java.util.List;

/**
 * Created by dmitry on 13.05.18.
 */

public interface Api {
    @ApiRequest(entity = "user", method = "register")
    @ApiRequireAuthorization
    void registerUser();

    @ApiRequest(entity = "decks", method = "save")
    @ApiRequireAuthorization
    void saveDecks(@ApiParameter("decks") List<SyncDeck> decks);

    @ApiRequest(entity = "decks", method = "query")
    @ApiRequireAuthorization
    List<SyncDeck> queryDecks(@ApiParameter("decks") List<String> deckNames);

    @ApiRequest(entity = "decks", method = "delete")
    @ApiRequireAuthorization
    void deleteDecks(@ApiParameter("decks") List<String> deckNames);

    @ApiRequest(entity = "decks", method = "dump")
    @ApiRequireAuthorization
    List<SyncDeckDump> dumpDecks();

    @ApiRequest(entity = "cards", method = "save")
    @ApiRequireAuthorization
    void saveCards(@ApiParameter("cards") List<SyncCard> cards);

    @ApiRequest(entity = "cards", method = "query")
    @ApiRequireAuthorization
    List<SyncCard> queryCards(@ApiParameter("cards") List<SyncCardId> cardIds);

    @ApiRequest(entity = "cards", method = "delete")
    @ApiRequireAuthorization
    void deleteCards(@ApiParameter("cards") List<SyncCardId> cardIds);

    @ApiRequest(entity = "cards", method = "dump")
    @ApiRequireAuthorization
    List<SyncCardDump> dumpCards();
}
