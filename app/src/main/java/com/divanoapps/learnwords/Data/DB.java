package com.divanoapps.learnwords.Data;

import android.os.AsyncTask;
import android.os.Environment;

import com.divanoapps.learnwords.Entities.Deck;

import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 08.10.17.
 */

public class DB {
    //////////////////////
    // Singleton interface

    private static volatile DB instance;

    public static DB getInstance() {
        DB localInstance = instance;
        if (localInstance == null) {
            synchronized (DB.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DB();
                }
            }
        }
        return localInstance;
    }

    ///////////////////////////////
    // Changes and change listeners

    public interface AllDecksLoadedListener {
        void onAllDecksLoaded();
    }

    public interface DeckChangedListener {
        void onDeckChanged(String deckName);
    }

    private List<AllDecksLoadedListener> mAllDecksLoadedListeners;
    private List<DeckChangedListener> mDeckChangedListeners;

    public static void addListener(AllDecksLoadedListener listener) {
        getInstance().mAllDecksLoadedListeners.add(listener);
    }

    public static void addListener(DeckChangedListener listener) {
        getInstance().mDeckChangedListeners.add(listener);
    }

    private void notifyAllDecksLoaded() {
        for (AllDecksLoadedListener listener : mAllDecksLoadedListeners)
            listener.onAllDecksLoaded();
    }

    private void notifyDeckChanged(String deckName) {
        for (DeckChangedListener listener : mDeckChangedListeners)
            listener.onDeckChanged(deckName);
    }

    private void initializeListenerLists() {
        mAllDecksLoadedListeners = new LinkedList<>();
        mDeckChangedListeners = new LinkedList<>();
    }

    //////////////////////
    // DB primary business

    // Exceptions

    public static class DeckNotFoundException extends Exception {
        DeckNotFoundException(String deckName) {
            super("Deck with name " + deckName + " not found");
        }
    }

    // Private

    private List<Deck> mDecks;

    private DB() {
        initializeListenerLists();
        mDecks = new ArrayList<>();
    }

    private static File getDeckStorageDirectory() {
        return new File(Environment.getExternalStorageDirectory(), "Learn words");
    }

    private List<String> getListOfNamesOfExistingDecks() {
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

    private static class UnableToLoadDeckException extends Exception {
        UnableToLoadDeckException(String deckName) {
            super("Unable to load deck with name " + deckName);
        }
    }

    private static String getFileContent(File file) throws UnableToLoadDeckException {
        String content = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
            throw new UnableToLoadDeckException(file.getName());
        }
        return content;
    }

    private static Deck loadDeck(String deckName) throws UnableToLoadDeckException {
        try {
            return Deck.fromJson(deckName, getFileContent(new File(getDeckStorageDirectory(), deckName + ".json")));
        } catch (JSONException e) {
            throw new UnableToLoadDeckException(deckName);
        }
    }

    private void private_loadAll() {
        getInstance().mDecks = new ArrayList<>();

        for (String deckName : getInstance().getListOfNamesOfExistingDecks()) {
            Deck deck = null;
            try {
                deck = loadDeck(deckName);
            } catch (UnableToLoadDeckException e) {
                e.printStackTrace();
            }
            getInstance().mDecks.add(deck);
        }

        getInstance().notifyAllDecksLoaded();
    }

    private static class LoadAllDecksTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DB.getInstance().private_loadAll();
            return null;
        }
    }

    private Deck private_getDeck(String name) throws DeckNotFoundException {
        for (Deck deck : mDecks)
            if (deck.getName().equals(name))
                return deck;
        throw new DeckNotFoundException(name);
    }

    private List<Deck> private_getDecks() {
        return Collections.unmodifiableList(mDecks);
    }

    // Public

    public static void loadAll() {
        new LoadAllDecksTask().execute();
    }

    public static List<Deck> getDecks() {
        return getInstance().private_getDecks();
    }

    public static Deck getDeck(String name) throws DeckNotFoundException {
        return getInstance().private_getDeck(name);
    }
}
