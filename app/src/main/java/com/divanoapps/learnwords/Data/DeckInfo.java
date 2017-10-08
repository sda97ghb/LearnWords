package com.divanoapps.learnwords.Data;

/**
 * Created by dmitry on 08.10.17.
 */

public class DeckInfo {
    private String mName;
    private int mNumberOfCards;

    DeckInfo(String name, int nubmerOfCards) {
        mName = name;
        mNumberOfCards = nubmerOfCards;
    }

    public String getName() {
        return mName;
    }

    public int getNumberOfCards() {
        return mNumberOfCards;
    }
}
