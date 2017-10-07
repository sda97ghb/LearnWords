package com.divanoapps.learnwords.Entities;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deck as a named bunch of cards.
 *
 * @author Dmitry Smirnov
 */
public class Deck {
    private String mName;
    private List<Card> mCards;

    public Deck(@NonNull String name)
    {
        mName = name;
        mCards = new ArrayList<>();
    }

    public Deck(Deck other)
    {
        mName = other.getName();
        mCards = new ArrayList<>(other.getCards());
    }

    public static Deck fromJson(@NonNull String name, @NonNull String json) throws JSONException {
        Deck deck = new Deck(name);
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); ++ i) {
            JSONObject object = jsonArray.getJSONObject(i);
            Card card = Card.fromJson(name, object);
            deck.getCards().add(card);
        }
        return deck;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public List<Card> getCards()
    {
        return mCards;
    }
}
