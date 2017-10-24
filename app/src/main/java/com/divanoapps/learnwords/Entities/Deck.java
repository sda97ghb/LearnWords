package com.divanoapps.learnwords.Entities;

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
    private List<Card> mCards;

    public Deck() {
        mName = "";
        mCards = new ArrayList<>();
    }

    public Deck(Deck other) {
        mName = other.getName();
        mCards = new ArrayList<>(other.getCards());
    }

    public static Deck fromJson(@NonNull String name, @NonNull String json) throws JSONException {
        Deck.Builder builder = new Deck.Builder();
        builder.setName(name);
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); ++ i) {
            JSONObject object = jsonArray.getJSONObject(i);
            builder.addCard(Card.fromJson(name, object));
        }
        return builder.build();
    }

    public String getName()
    {
        return mName;
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
