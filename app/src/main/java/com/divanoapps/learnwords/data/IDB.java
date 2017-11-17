package com.divanoapps.learnwords.data;

import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;

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
    void modifyDeck(DeckId id, Map<String, Object> properties) throws ForbiddenException, NotFoundException;
    void deleteDeck(DeckId id) throws ForbiddenException, NotFoundException;

    Card getCard(CardId id) throws NotFoundException;
    void saveCard(Card card) throws ForbiddenException;
    void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException;
    void updateCard(CardId id, Card newCard) throws ForbiddenException, NotFoundException;
    void deleteCard(CardId id) throws ForbiddenException, NotFoundException;
}
