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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    private static File getFileForDeck(String deckName) {
        return new File(getDeckStorageDirectory(), deckName + ".json");
    }

    private void loadAllDecks() throws UnableToReadFileException, JSONException {
        mDecks = new LinkedList<>();

        for (String deckName : getListOfNamesOfFilesOfExistingDecks())
            mDecks.add(Deck.fromJson(getTextFileContent(getFileForDeck(deckName))));
    }

    ////////////////////////////////////////////////////////////////
    // Public

    void initialize() throws UnableToReadFileException, JSONException {
        loadAllDecks();
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
        throw new ForbiddenException();
    }

    @Override
    public void modifyDeck(DeckId id, Map<String, Object> properties) throws ForbiddenException {
        throw new ForbiddenException();
    }

    @Override
    public void deleteDeck(DeckId id) throws ForbiddenException {
        throw new ForbiddenException();
    }

    @Override
    public Card getCard(CardId id) throws NotFoundException {
        for (Card card : getDeck(new DeckId(id.getDeckName())).getCards())
            if (card.getId().equals(id))
                return card;
        throw new NotFoundException();
    }

    @Override
    public void saveCard(Card deck) throws ForbiddenException {
        throw new ForbiddenException();
    }

    @Override
    public void modifyCard(CardId id, Map<String, Object> properties) throws ForbiddenException {
        throw new ForbiddenException();
    }

    @Override
    public void deleteCard(CardId id) throws ForbiddenException {
        throw new ForbiddenException();
    }
}
