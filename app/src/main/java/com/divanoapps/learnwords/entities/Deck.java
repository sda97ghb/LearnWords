package com.divanoapps.learnwords.entities;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck as a named bunch of cards.
 *
 * @author Dmitry Smirnov
 */
public class Deck {
    private String mName;
    private String mLanguageFrom;
    private String mLanguageTo;
    private List<Card> mCards;

    public Deck() {
        mName = getDefaultName();
        mLanguageFrom = getDefaultLanguageFrom();
        mLanguageTo = getDefaultLanguageTo();
        mCards = new ArrayList<>();
    }

    public Deck(Deck other) {
        mName = other.getName();
        mLanguageFrom = other.getLanguageFrom();
        mLanguageTo = other.getLanguageTo();
        mCards = new ArrayList<>(other.getCards());
    }

    public static Deck fromJson(@NonNull String json) throws JSONException {
        final Deck.Builder builder = new Deck.Builder();

        JSONObject deckJsonObject = new JSONObject(json);

        String name         = deckJsonObject.getString("name");
        String languageFrom = deckJsonObject.getString("languageFrom");
        String languageTo   = deckJsonObject.getString("languageTo");

        builder.setName(name);
        builder.setLanguageFrom(languageFrom);
        builder.setLanguageTo(languageTo);

        JSONArray cardsJsonArray = deckJsonObject.getJSONArray("cards");
        if (cardsJsonArray != null) {
            for (int i = 0; i < cardsJsonArray.length(); ++ i)
                builder.addCard(Card.fromJson(name, cardsJsonArray.getJSONObject(i)));
        }
        return builder.build();
    }

    public JSONObject toJson() throws JSONException {
        JSONObject deckJson = new JSONObject();
        deckJson.put("name", getName());
        deckJson.put("languageFrom", getLanguageFrom());
        deckJson.put("languageTo", getLanguageTo());

        JSONArray cardsJson = new JSONArray();
        for (Card card : getCards())
            cardsJson.put(card.toJson());
        deckJson.put("cards", cardsJson);

        return deckJson;
    }

    public static String getDefaultName() {
        return "No name";
    }

    public static String getDefaultLanguageFrom() {
        return "English";
    }

    public static String getDefaultLanguageTo() {
        return "English";
    }

    public DeckId getId() {
        return new DeckId(getName());
    }

    public DeckShort getShortDescription() {
        return new DeckShort(getName(), getNumberOfCards(), getNumberOfHiddenCards(),
                getLanguageFrom(), getLanguageTo());
    }

    public String getName()
    {
        return mName;
    }

    public String getLanguageFrom() {
        return mLanguageFrom;
    }

    public String getLanguageTo() {
        return mLanguageTo;
    }

    public List<Card> getCards()
    {
        return Collections.unmodifiableList(mCards);
    }

    public int getNumberOfCards() {
        return mCards.size();
    }

    public int getNumberOfHiddenCards() {
        int i = 0;
        for (Card card : mCards)
            if (card.isHidden())
                ++ i;
        return i;
    }

    public static class Builder {
        private Deck mDeck;

        public Builder() {
            mDeck = new Deck();
        }

        public Builder(Deck other) {
            mDeck = new Deck(other);
        }

        public Builder setName(String name) {
            mDeck.mName = name;
            return this;
        }

        public Builder setLanguageFrom(String language) {
            mDeck.mLanguageFrom = language;
            return this;
        }

        public Builder setLanguageTo(String language) {
            mDeck.mLanguageTo = language;
            return this;
        }

        public Builder setCards(List<Card> cards) {
            mDeck.mCards = new ArrayList<>(cards);
            return this;
        }

        public Builder addCard(Card card) {
            mDeck.mCards.add(card);
            return this;
        }

        public Deck build() {
            return new Deck(mDeck);
        }
    }
}
