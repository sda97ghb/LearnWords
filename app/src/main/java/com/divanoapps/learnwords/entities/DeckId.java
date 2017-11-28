package com.divanoapps.learnwords.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by dmitry on 30.10.17.
 */

public class DeckId implements Serializable {
    private String mName;

    public DeckId(String deckName) {
        mName = deckName;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeckId) {
            DeckId other = (DeckId) obj;
            return other.getName().equals(getName());
        }

        if (obj instanceof Deck)
            return equals(((Deck) obj).getId());

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
