package com.divanoapps.learnwords.Data;

import android.os.AsyncTask;
import android.os.Environment;

import com.divanoapps.learnwords.Entities.Deck;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
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

    private void initializeListeners() {
        mAllDecksLoadedListeners = new LinkedList<>();
        mDeckChangedListeners = new LinkedList<>();
    }

    //////////////////////
    // DB primary business

    private List<Deck> mDecks;

    private DB() {
        initializeListeners();
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

    private Deck loadDeck(String name) throws UnableToLoadDeckException {
        File deckFile = new File(getDeckStorageDirectory(), name + ".json");
        String jsonString = null;
        try {
            InputStream inputStream = new FileInputStream(deckFile);
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            jsonString = new String(bytes, "UTF-8");
        } catch (IOException e) {
            throw new UnableToLoadDeckException(name);
        }
        try {
            return Deck.fromJson(name, jsonString);
        } catch (JSONException e) {
            throw new UnableToLoadDeckException(name);
        }
    }

    public void loadAll() {
        mDecks = new ArrayList<>();

        for (String deckName : getListOfNamesOfExistingDecks()) {
            Deck deck = null;
            try {
                deck = loadDeck(deckName);
            } catch (UnableToLoadDeckException e) {
                e.printStackTrace();
            }
            mDecks.add(deck);
        }

        notifyAllDecksLoaded();
    }

    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DB.getInstance().loadAll();
            return null;
        }
    }

    public List<DeckInfo> getListOfDeckInfos() {
        List<DeckInfo> infos = new ArrayList<>();
        for (Deck deck : mDecks)
            infos.add(new DeckInfo(deck.getName(), deck.getCards().size()));
        return infos;
    }

    public static class DeckNotFoundException extends Exception {
        DeckNotFoundException(String deckName) {
            super("Deck with name " + deckName + " not found");
        }
    }

    public Deck getDeck(String name) throws DeckNotFoundException {
        for (Deck deck : mDecks)
            if (deck.getName().equals(name))
                return new Deck(deck);
        throw new DeckNotFoundException(name);
    }
}
