package com.divanoapps.learnwords.Data;

import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.CardId;
import com.divanoapps.learnwords.Entities.Deck;
import com.divanoapps.learnwords.Entities.DeckId;
import com.divanoapps.learnwords.Entities.DeckShort;

import java.util.List;
import java.util.Map;

/**
 * Common interface for all DB implementations such as LocalDB and NetDB.
 */

interface IDB {
    class NotFoundException extends Exception {
        NotFoundException() {
            super("Not found.");
        }
    }

    class ForbiddenException extends Exception {
        ForbiddenException() {
            super("Forbidden.");
        }
    }

    List<DeckShort> getDecks() throws Exception;

    Deck getDeck(DeckId id) throws NotFoundException;
    void saveDeck(Deck deck) throws ForbiddenException;
    void modifyDeck(DeckId id, Map<String, Object> properties) throws ForbiddenException;
    void deleteDeck(DeckId id) throws ForbiddenException;

    Card getCard(CardId id) throws NotFoundException;
    void saveCard(Card deck) throws ForbiddenException;
    void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException;
    void deleteCard(CardId id) throws ForbiddenException;
}
