package com.divanoapps.learnwords.entities;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a deck as a named bunch of cards.
 *
 * @author Dmitry Smirnov
 */
public class Deck {
    private String mName;
    private String mLanguageFrom;
    private String mLanguageTo;
    private Map<CardId, Card> mCards;

    public Deck() {
        mName = getDefaultName();
        mLanguageFrom = getDefaultLanguageFrom();
        mLanguageTo = getDefaultLanguageTo();
        mCards = new LinkedHashMap<>();
    }

    public Deck(@NonNull Deck other) {
        mName = other.getName();
        mLanguageFrom = other.getLanguageFrom();
        mLanguageTo = other.getLanguageTo();
        mCards = new LinkedHashMap<>(other.getCards());
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
        for (Map.Entry<CardId, Card> entry : getCards().entrySet())
            cardsJson.put(entry.getValue().toJson());
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
        return new DeckShort(
                getName(),
                getNumberOfCards(), getNumberOfHiddenCards(),
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

    public Map<CardId, Card> getCards()
    {
        return Collections.unmodifiableMap(mCards);
    }

    public List<Card> getCardsAsList() {
        List<Card> list = new LinkedList<>();
        for (Map.Entry<CardId, Card> entry : mCards.entrySet())
            list.add(entry.getValue());
        return Collections.unmodifiableList(list);
    }

    public int getNumberOfCards() {
        return mCards.size();
    }

    public int getNumberOfHiddenCards() {
        int i = 0;
        for (Map.Entry<CardId, Card> entry : mCards.entrySet())
            if (entry.getValue().isHidden())
                ++ i;
        return i;
    }

    public static class Builder {
        private Deck mDeck;

        public Builder() {
            mDeck = new Deck();
        }

        public Builder(@NonNull Deck other) {
            mDeck = new Deck(other);
        }

        public Builder setName(@NonNull String name) {
            mDeck.mName = name;
            return this;
        }

        public Builder setLanguageFrom(@NonNull String language) {
            mDeck.mLanguageFrom = language;
            return this;
        }

        public Builder setLanguageTo(@NonNull String language) {
            mDeck.mLanguageTo = language;
            return this;
        }

        public Builder setCards(@NonNull Map<CardId, Card> cards) {
            mDeck.mCards = new LinkedHashMap<>(cards);
            return this;
        }

        public Builder addCard(@NonNull Card card) {
            mDeck.mCards.put(card.getId(), card);
            return this;
        }

        public Deck build() {
            return new Deck(mDeck);
        }
    }
}
