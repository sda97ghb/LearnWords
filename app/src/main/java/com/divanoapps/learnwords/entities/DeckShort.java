package com.divanoapps.learnwords.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static DeckShort fromJson(JSONObject json) throws JSONException {
        return new DeckShort(
                json.getString("name"),
                json.getInt("numberOfCards"),
                json.getInt("numberOfHiddenCards"),
                json.getString("languageFrom"),
                json.getString("languageTo"));
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
