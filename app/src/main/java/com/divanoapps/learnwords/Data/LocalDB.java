package com.divanoapps.learnwords.Data;

import android.os.Environment;

import com.divanoapps.learnwords.Entities.Card;
import com.divanoapps.learnwords.Entities.CardId;
import com.divanoapps.learnwords.Entities.Deck;
import com.divanoapps.learnwords.Entities.DeckId;
import com.divanoapps.learnwords.Entities.DeckShort;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * DB implementation that works with local storage.
 */

class LocalDB implements IDB {
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

    private List<Deck> mDecks = new ArrayList<>();

    private static File getDeckStorageDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "Learn words");
    }

    private static List<String> getListOfNamesOfFilesOfExistingDecks() {
        File[] deckFiles = getDeckStorageDirectory().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

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
        mDecks = new LinkedList<>();

        for (String deckName : getListOfNamesOfFilesOfExistingDecks())
            mDecks.add(Deck.fromJson(getTextFileContent(getFileForDeck(deckName))));
    }

    private void saveDeckToFile(Deck deck) throws JSONException, IOException {
        saveTextToFile(getFileForDeck(deck), deck.toJson().toString());
    }

    ////////////////////////////////////////////////////////////////
    // Public

    void initialize() throws UnableToReadFileException, JSONException {
        if (mIsInitialized)
            return;

        loadAllDecks();

        mIsInitialized = true;
    }

    @Override
    public List<DeckShort> getDecks() throws Exception {
        List<DeckShort> decks = new LinkedList<>();
        for (Deck deck : mDecks)
            decks.add(deck.getShortDescription());
        return decks;
    }

    @Override
    public Deck getDeck(DeckId id) throws NotFoundException {
        for (Deck deck : mDecks)
            if (deck.getId().equals(id))
                return deck;
        throw new NotFoundException();
    }

    @Override
    public void saveDeck(Deck deck) throws ForbiddenException {
        try {
            deleteDeck(deck.getId());
        } catch (NotFoundException e) {
            // we are saving deck with this name at the first time
        }
        try {
            saveDeckToFile(deck);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            throw new ForbiddenException();
        }
        mDecks.add(deck);
    }

    @Override
    public void modifyDeck(DeckId id, Map<String, Object> properties) throws ForbiddenException, NotFoundException {
        // throws NotFoundException if deck with this id doesn't exists
        Deck.Builder builder = new Deck.Builder(getDeck(id));

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String property = entry.getKey();
            Object value = entry.getValue();
            switch (property) {
                case "name": {
                    if (value instanceof String)
                        builder.setName((String) value);
                    else
                        throw new ForbiddenException();
                } break;
                case "languageFrom": {
                    if (value instanceof String)
                        builder.setLanguageFrom((String) value);
                    else
                        throw new ForbiddenException();
                }
                case "languageTo": {
                    if (value instanceof String)
                        builder.setLanguageTo((String) value);
                    else
                        throw new ForbiddenException();
                }
                case "cards": {
                    if (value instanceof List<?>) {
                        List<?> list = (List<?>) value;
                        if (list.isEmpty())
                            builder.setCards(new LinkedList<Card>());
                        else if (list.get(0) instanceof Card)
                            builder.setCards((List<Card>) list);
                        else
                            throw new ForbiddenException();
                    }
                }
                default: throw new ForbiddenException();
            }
        }

        deleteDeck(id);
        saveDeck(builder.build());
    }

    @Override
    public void deleteDeck(DeckId id) throws ForbiddenException, NotFoundException {
        // if deck is not found throws NotFoundException
        Deck deckToDelete = getDeck(id);

        if (!getFileForDeck(id.getName()).delete())
            throw new ForbiddenException();
        mDecks.remove(deckToDelete);
    }

    @Override
    public Card getCard(CardId id) throws NotFoundException {
        for (Card card : getDeck(new DeckId(id.getDeckName())).getCards())
            if (card.getId().equals(id))
                return card;
        throw new NotFoundException();
    }

    @Override
    public void saveCard(Card card) throws ForbiddenException {
        try {
            deleteCard(card.getId());
        } catch (NotFoundException e) {
            // we are saving card with this id at the first time
        }

        try {
            DeckId deckId = new DeckId(card.getDeckName());
            saveDeck(new Deck.Builder(getDeck(deckId)).addCard(card).build());
        } catch (NotFoundException e) {
            throw new ForbiddenException();
        }
    }

    @Override
    public void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException {
        throw new ForbiddenException();
    }

    @Override
    public void updateCard(CardId id, Card newCard) throws ForbiddenException, NotFoundException {
        deleteCard(id);
        saveCard(newCard);
    }

    @Override
    public void deleteCard(CardId id) throws ForbiddenException, NotFoundException {
        // if deck not found throws NotFoundException
        Deck deck = getDeck(new DeckId(id.getDeckName()));

        List<Card> cards = new LinkedList<>(deck.getCards());
        Card cardToDelete = getCard(id); // if card not found throws NotFoundException
        cards.remove(cardToDelete);

        saveDeck(new Deck.Builder(deck).setCards(cards).build());
    }
}
