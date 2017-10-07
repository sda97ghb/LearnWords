package com.divanoapps.learnwords;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class DataProvider {
    //TODO: remove itemTouchHelper
    public ItemTouchHelper itemTouchHelper;
    private static volatile DataProvider instance;

    public static DataProvider getInstance() {
        DataProvider localInstance = instance;
        if (localInstance == null) {
            synchronized (DataProvider.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataProvider();
                    instance.load();
                }
            }
        }
        return localInstance;
    }

    private ArrayList<Deck> decks = new ArrayList<>();

    public void load() {
        for (int i = 0; i < 30; ++ i)
            this.addDeck(new Deck(new StringBuilder().append("Deck ").append(i).toString()));
    }

    public void addDeck(Deck deck)
    {
        this.decks.add(deck);
    }

    public ArrayList<Deck> getDeckList()
    {
        return this.decks;
    }
}
