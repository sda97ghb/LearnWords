package com.divanoapps.learnwords.Entities;

/**
 * Created by dmitry on 30.10.17.
 */

public class DeckShort {
    private String mName;
    private int mNumberOfCards;
    private int mNumberOfHiddenCards;
    private String mLanguageFrom;
    private String mLanguageTo;

    public DeckShort(String name, int numberOfCards, int numberOfHiddenCards,
              String languageFrom, String languageTo) {
        mName = name;
        mNumberOfCards = numberOfCards;
        mNumberOfHiddenCards = numberOfHiddenCards;
        mLanguageFrom = languageFrom;
        mLanguageTo = languageTo;
    }

    public DeckId getId() {
        return new DeckId(getName());
    }

    public String getName() {
        return mName;
    }

    public int getNumberOfCards() {
        return mNumberOfCards;
    }

    public int getNumberOfHiddenCards() {
        return mNumberOfHiddenCards;
    }

    public String getLanguageFrom() {
        return mLanguageFrom;
    }

    public String getLanguageTo() {
        return mLanguageTo;
    }
}
