package com.divanoapps.learnwords.Data;

import android.os.AsyncTask;
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
 * Created by dmitry on 30.10.17.
 */

public class LocalDB implements IDB {
    ////////////////////////////////////////////////////////////////
    // Exceptions

    public static class UnableToReadFileException extends Exception {
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

    private static List<String> getListOfNamesOfExistingDecks() {
        File[] deckFiles = getDeckStorageDirectory().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

        if (deckFiles == null)
            return new ArrayList<>();

        List<String> deckNames = new ArrayList<>();
        for (File file : deckFiles) {
            String name = file.getName();
            name = name.substring(0, name.length() - 5);
            deckNames.add(name);
        }

        return deckNames;
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

    private static Deck loadDeckFromFile(String deckName) throws UnableToReadFileException, JSONException {
        return Deck.fromJson(deckName, getTextFileContent(getFileForDeck(deckName)));
    }

    private void loadAll() throws UnableToReadFileException, JSONException {
        mDecks = new LinkedList<>();

        for (String deckName : getListOfNamesOfExistingDecks())
            mDecks.add(loadDeckFromFile(deckName));
    }

    ////////////////////////////////////////////////////////////////
    // Public

    public void initialize() throws UnableToReadFileException, JSONException {
        loadAll();
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
