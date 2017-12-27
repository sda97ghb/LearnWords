package com.divanoapps.learnwords.data;

import android.os.Environment;

import com.divanoapps.learnwords.entities.Card;
import com.divanoapps.learnwords.entities.CardId;
import com.divanoapps.learnwords.entities.Deck;
import com.divanoapps.learnwords.entities.DeckId;
import com.divanoapps.learnwords.entities.DeckShort;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.divanoapps.learnwords.auxiliary.Setter;

/**
 * DB implementation that works with local storage.
 */

public class LocalDB implements IDB {
    ////////////////////////////////////////////////////////////////
    // Exceptions

    static class UnableToReadFileException extends Exception {
        UnableToReadFileException(String filename) {
            super("Unable to read file " + filename);
        }
    }

    ////////////////////////////////////////////////////////////////
    // Private

    private boolean mIsInitialized = false;

    private Map<DeckId, Deck> mDecks = new LinkedHashMap<>();

    private static File getDeckStorageDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "Learn words");
    }

    private static List<String> getListOfNamesOfFilesOfExistingDecks() {
        File[] deckFiles = getDeckStorageDirectory().listFiles((dir, name) -> name.endsWith(".json"));

        if (deckFiles == null)
            return new ArrayList<>();

        List<String> deckFileNames = new ArrayList<>();
        for (File file : deckFiles) {
            String name = file.getName();
            name = name.substring(0, name.length() - 5);
            deckFileNames.add(name);
        }

        return deckFileNames;
    }

    private static String getTextFileContent(File file) throws UnableToReadFileException {
        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes, "UTF-8");
        } catch (IOException e) {
            throw new UnableToReadFileException(file.getName());
        }
    }

    private static void saveTextToFile(File file, String content) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    private static File getFileForDeck(String deckName) {
        return new File(getDeckStorageDirectory(), deckName + ".json");
    }

    private static File getFileForDeck(Deck deck) {
        return getFileForDeck(deck.getName());
    }

    private void loadAllDecks() throws UnableToReadFileException, JSONException {
        mDecks = new LinkedHashMap<>();

        for (String deckName : getListOfNamesOfFilesOfExistingDecks()) {
            Deck deck = Deck.fromJson(getTextFileContent(getFileForDeck(deckName)));
            mDecks.put(deck.getId(), deck);
        }
    }

    private void saveDeckToFile(Deck deck) throws JSONException, IOException {
        saveTextToFile(getFileForDeck(deck), deck.toJson().toString());
    }

    private static <T> void checkAndSet(Class<T> clazz, Object value, Setter<T> setter) throws ForbiddenException {
        if (clazz.isInstance(value))
            setter.set(clazz.cast(value));
        else
            throw new ForbiddenException();
    }

    ////////////////////////////////////////////////////////////////
    // Public

    void initialize() throws UnableToReadFileException, JSONException {
        if (mIsInitialized)
            return;

        loadAllDecks();

        mIsInitialized = true;
    }

    /**
     * @return list of deck descriptions or empty list if there are no decks loaded.
     */
    @Override
    public List<DeckShort> getDecks() {
        List<DeckShort> decks = new LinkedList<>();
        for (Map.Entry<DeckId, Deck> entry : mDecks.entrySet())
            decks.add(entry.getValue().getShortDescription());
        return decks;
    }

    public boolean deckExists(DeckId id) {
        return mDecks.containsKey(id);
    }

    /**
     * Returns a deck with specified id or throws NotFoundException if it does not exists.
     * @param id required deck id
     * @return required deck
     * @throws NotFoundException when deck not found
     */
    @Override
    public Deck getDeck(DeckId id) throws NotFoundException {
        if (mDecks.containsKey(id))
            return mDecks.get(id);
        else
            throw new NotFoundException();
    }

    /**
     * Create new record in DB if there is no deck with the same id as argument deck id,
     * update existing record otherwise.
     * @param deck deck need to be stored
     * @throws ForbiddenException when the deck can't be saved (when unable to write to file)
     */
    @Override
    public void saveDeck(Deck deck) throws ForbiddenException {
        mDecks.put(deck.getId(), deck);
        try {
            saveDeckToFile(deck);
        } catch (JSONException | IOException e) {
            throw new ForbiddenException();
        }
    }

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
    @Override
    public void updateDeck(DeckId id, Deck newDeck) throws NotFoundException, AlreadyExistsException, ForbiddenException {
        if (!deckExists(id))
            throw new NotFoundException();

        if (!id.equals(newDeck.getId())) {
            if (deckExists(newDeck.getId()))
                throw new AlreadyExistsException();

            // Build new mDecks collection with replaced deck
            Map<DeckId, Deck> newDecks = new LinkedHashMap<>();
            for (Map.Entry<DeckId, Deck> entry : mDecks.entrySet()) {
                if (entry.getKey().equals(id))
                    newDecks.put(newDeck.getId(), newDeck);
                else
                    newDecks.put(entry.getKey(), entry.getValue());
            }
            mDecks = newDecks;

            // Delete old deck's file
            if (!getFileForDeck(id.getName()).delete())
                throw new ForbiddenException();

            // Save new deck to file
            try {
                saveDeckToFile(newDeck);
            } catch (JSONException | IOException e) {
                throw new ForbiddenException();
            }
        }
        else {
            saveDeck(newDeck);
        }
    }

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
     * @throws ForbiddenException when the deck can't be modified (when unable to write to file)
     */
    @Override
    public void modifyDeck(DeckId id, Map<String, Object> properties)
    throws NotFoundException, PropertyNotExistsException, AlreadyExistsException, ForbiddenException {
        Deck oldDeck = getDeck(id);
        Deck.Builder builder = new Deck.Builder(oldDeck);

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String property = entry.getKey();
            Object value = entry.getValue();
            switch (property) {
                case "name":         checkAndSet(String.class, value, builder::setName);         break;
                case "languageFrom": checkAndSet(String.class, value, builder::setLanguageFrom); break;
                case "languageTo":   checkAndSet(String.class, value, builder::setLanguageTo);   break;
                case "cards": {
                    if (value instanceof List<?>) {
                        List<?> list = (List<?>) value;
                        if (list.isEmpty())
                            builder.setCards(new LinkedHashMap<>());
                        else if (list.get(0) instanceof Card) {
                            List<Card> cardsAsList = (List<Card>) list;
                            LinkedHashMap<CardId, Card> cardsAsMap = new LinkedHashMap<>();
                            for (Card card : cardsAsList)
                                cardsAsMap.put(card.getId(), card);
                            builder.setCards(cardsAsMap);
                        }
                        else
                            throw new ForbiddenException();
                    }
                    else if (value instanceof Map<?, ?>) {
                        Map<?, ?> map = (Map<?, ?>) value;
                        if (map.isEmpty())
                            builder.setCards(new LinkedHashMap<>());
                        else if (map.keySet().iterator().next().getClass().equals(CardId.class) &&
                                 map.values().iterator().next().getClass().equals(Card.class)) {
                            builder.setCards((Map<CardId, Card>) map);
                        }
                        else
                            throw new ForbiddenException();
                    }
                    else
                        throw new ForbiddenException();
                } break;
                default: throw new PropertyNotExistsException();
            }
        }

        updateDeck(id, builder.build());
    }

    /**
     * Delete deck with id if it exists, throws exception otherwise.
     * @param id id of deck that need to delete
     * @throws NotFoundException when deck with required id does not exist
     * @throws ForbiddenException when the deck can't be deleted (when unable to delete file)
     */
    @Override
    public void deleteDeck(DeckId id) throws NotFoundException, ForbiddenException {
        if (mDecks.containsKey(id))
            mDecks.remove(id);
        else
            throw new NotFoundException();
        if (!getFileForDeck(id.getName()).delete())
            throw new ForbiddenException();
    }

    public boolean cardExists(CardId id) {
        try {
            return getDeck(new DeckId(id.getDeckName())).getCards().containsKey(id);
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * Returns a card with required id or throws NotFoundException if card does not exist.
     * @param id required card id
     * @return required card
     * @throws NotFoundException when card not found
     */
    @Override
    public Card getCard(CardId id) throws NotFoundException {
        Map<CardId, Card> cards = getDeck(new DeckId(id.getDeckName())).getCards();
        if (cards.containsKey(id))
            return cards.get(id);
        else
            throw new NotFoundException();
    }

    /**
     * Create new record in DB if there is no card with the same id as argument card id,
     * update existing record otherwise.
     * @param card card need to be stored
     * @throws NotFoundException when deck which should contain the card does not exist
     * @throws ForbiddenException when the card can't be saved (when unable to write to file)
     */
    @Override
    public void saveCard(Card card) throws ForbiddenException, NotFoundException {
        Deck oldDeck = getDeck(new DeckId(card.getDeckName()));

        Map<CardId, Card> cards = new LinkedHashMap<>(oldDeck.getCards());
        cards.put(card.getId(), card);

        saveDeck(new Deck.Builder(oldDeck).setCards(cards).build());
    }

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
    @Override
    public void updateCard(CardId id, Card newCard) throws NotFoundException, AlreadyExistsException, ForbiddenException {
        if (!cardExists(id))
            throw new NotFoundException();

        if (!id.equals(newCard.getId())) {
            if (cardExists(newCard.getId()))
                throw new AlreadyExistsException();

            Deck oldDeck = getDeck(new DeckId(id.getDeckName()));
            Map<CardId, Card> oldCards = oldDeck.getCards();
            Map<CardId, Card> newCards = new LinkedHashMap<>();
            for (Map.Entry<CardId, Card> entry : oldCards.entrySet()) {
                if (entry.getKey().equals(id))
                    newCards.put(newCard.getId(), newCard);
                else
                    newCards.put(entry.getKey(), entry.getValue());
            }
            Deck newDeck = new Deck.Builder(oldDeck).setCards(newCards).build();
            saveDeck(newDeck);
        }
        else {
            saveCard(newCard);
        }
    }

    /**
     * Change properties of existing card.
     * Properties can be:
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
     * @throws ForbiddenException when the card can't be modified (when unable to write to file)
     */
    @Override
    public void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException, NotFoundException, AlreadyExistsException, PropertyNotExistsException {
        Card.Builder builder = new Card.Builder(getCard(id));

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            Object value = entry.getValue();
            switch (entry.getKey()) {
                case "word":               checkAndSet(String.class,  value, builder::setWord);               break;
                case "wordComment":        checkAndSet(String.class,  value, builder::setWordComment);        break;
                case "translation":        checkAndSet(String.class,  value, builder::setTranslation);        break;
                case "translationComment": checkAndSet(String.class,  value, builder::setTranslationComment); break;
                case "difficulty":         checkAndSet(Integer.class, value, builder::setDifficulty);         break;
                case "isHidden":           checkAndSet(Boolean.class, value, builder::setHidden);             break;
                case "cropPicture":        checkAndSet(Boolean.class, value, builder::setCropPicture);        break;
                default: throw new PropertyNotExistsException();
            }
        }

        updateCard(id, builder.build());
    }

    /**
     * Delete card with id if it exists, throws exception otherwise.
     * @param id id of card that need to be deleted
     * @throws NotFoundException when card with required id does not exist
     * @throws ForbiddenException when the card can't be deleted (when unable to delete file)
     */
    @Override
    public void deleteCard(CardId id) throws ForbiddenException, NotFoundException {
        if (!cardExists(id))
            throw new NotFoundException();

        Deck oldDeck = getDeck(new DeckId(id.getDeckName()));

        Map<CardId, Card> cards = new LinkedHashMap<>(oldDeck.getCards());
        cards.remove(id);

        saveDeck(new Deck.Builder(oldDeck).setCards(cards).build());
    }
}
