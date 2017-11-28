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

public interface IDB {
    class ForbiddenException extends Exception {
        ForbiddenException() {
            super("Forbidden.");
        }
    }

    class NotFoundException extends Exception {
        NotFoundException() {
            super("Not found.");
        }
    }

    class AlreadyExistsException extends Exception {
        AlreadyExistsException() {
            super("Already exists.");
        }
    }

    class PropertyNotExistsException extends Exception {
        PropertyNotExistsException() {
            super("Property does not exist.");
        }
    }

    /**
     * @return list of deck descriptions or empty list if there are no decks loaded.
     */
    List<DeckShort> getDecks();

    /**
     * Returns a deck with specified id or throws NotFoundException if it does not exist.
     * @param id required deck id
     * @return required deck
     * @throws NotFoundException when deck not found
     */
    Deck getDeck(DeckId id) throws NotFoundException;

    /**
     * Create new record in DB if there is no deck with the same id as argument deck id,
     * update existing record otherwise.
     * @param deck deck need to be stored
     * @throws ForbiddenException when the deck can't be saved (for example, when unable
     * to write to file for local DB or there is no connection with remote DB)
     */
    void saveDeck(Deck deck) throws ForbiddenException;

    /**
     * Saves new deck instead of deck with id if it exist,
     * throws NotFoundException when deck with id does not exist,
     * throws AlreadyExistsException when deck with id as new deck's id already exists.
     * @param id      previous id
     * @param newDeck new deck to be stored
     * @throws NotFoundException when deck with specified id does not exist
     * @throws AlreadyExistsException when deck with id as newDeck's id already exists
     * @throws ForbiddenException when the deck can't be updated (when unable to write to file)
     */
    void updateDeck(DeckId id, Deck newDeck) throws NotFoundException, AlreadyExistsException, ForbiddenException;

    /**
     * Change properties of existing deck.
     * Properties to change can be:
     *  - name as String
     *  - languageFrom as String
     *  - languageTo as String
     *  - cards as List<Cards>
     * @param id         id of deck to change
     * @param properties map where each entry is a pair of property name (as String) and
     *                   property value (type of value depends on property)
     * @throws NotFoundException when deck with required id does not exist
     * @throws PropertyNotExistsException when properties contains nonexistent property
     * @throws AlreadyExistsException when id of changed deck conflicts with already existing deck
     * @throws ForbiddenException when the deck can't be modified (for example,
     * when unable to write to file for local DB or there is no connection with remote DB)
     */
    void modifyDeck(DeckId id, Map<String, Object> properties) throws NotFoundException, PropertyNotExistsException, AlreadyExistsException, ForbiddenException;

    /**
     * Delete deck with id if it exists, throws exception otherwise.
     * @param id id of deck that need to delete
     * @throws NotFoundException when deck with required id does not exist
     * @throws ForbiddenException when the deck can't be deleted (for example,
     * when unable to delete file for local DB or there is no connection with remote DB)
     */
    void deleteDeck(DeckId id) throws NotFoundException, ForbiddenException;

    /**
     * Returns a card with required id or throws NotFoundException if card does not exist.
     * @param id required card id
     * @return required card
     * @throws NotFoundException when card not found
     */
    Card getCard(CardId id) throws NotFoundException;

    /**
     * Create new record in DB if there is no card with the same id as argument card id,
     * update existing record otherwise.
     * @param card card need to be stored
     * @throws NotFoundException when deck which should contain the card does not exist
     * @throws ForbiddenException when the card can't be saved (when unable to write to file)
     */
    void saveCard(Card card) throws NotFoundException, ForbiddenException;

    /**
     * Saves new card instead of card with id if it exist,
     * throws NotFoundException when card with id does not exist,
     * throws AlreadyExistsException when card with id as new card's id already exists.
     * @param id      previous id
     * @param newCard new card to be stored
     * @throws NotFoundException when card with specified id does not exist
     * @throws AlreadyExistsException when card with id as newCard's id already exists
     * @throws ForbiddenException when the card can't be updated (when unable to write to file)
     */
    void updateCard(CardId id, Card newCard) throws NotFoundException, AlreadyExistsException, ForbiddenException;

    /**
     * Change properties of existing card.
     * Properties to change can be:
     *  - word as String
     *  - wordComment as String
     *  - translation as String
     *  - translationComment as String
     *  - difficulty as Integer
     *  - isHidden as Boolean
     *  - cropPicture as Boolean
     * @param id         id of card to change
     * @param properties map where each entry is a pair of property name (as String) and
     *                   property value (type of value depends on property)
     * @throws NotFoundException when card with required id does not exist
     * @throws PropertyNotExistsException when properties contains nonexistent property
     * @throws AlreadyExistsException when id of changed card conflicts with already existing card
     * @throws ForbiddenException when the card can't be modified (for example,
     * when unable to write to file for local DB or there is no connection with remote DB)
     */
    void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException, NotFoundException, AlreadyExistsException, PropertyNotExistsException;

    /**
     * Delete card with id if it exists, throws exception otherwise.
     * @param id id of card that need to be deleted
     * @throws NotFoundException when card with required id does not exist
     * @throws ForbiddenException when the card can't be deleted (for example,
     * when unable to delete file for local DB or there is no connection with remote DB)
     */
    void deleteCard(CardId id) throws ForbiddenException, NotFoundException;
}
