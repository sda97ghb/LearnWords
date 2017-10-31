package com.divanoapps.learnwords.Entities;

import java.io.Serializable;

/**
 * Created by dmitry on 30.10.17.
 */

public class DeckId implements Serializable {
    private String mDeckName;

    public DeckId(String deckName) {
        mDeckName = deckName;
    }

    public String getDeckName() {
        return mDeckName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeckId) {
            DeckId other = (DeckId) obj;
            return other.getDeckName().equals(getDeckName());
        }

        if (obj instanceof Deck)
            return equals(((Deck) obj).getId());

        return false;
    }
}
