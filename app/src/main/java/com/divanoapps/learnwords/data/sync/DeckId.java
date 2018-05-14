package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.wombat.Id;

import java.util.Objects;

/**
 * Created by dmitry on 14.05.18.
 */

public class DeckId implements Id {
    private String name;

    public DeckId() {
    }

    public DeckId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckId deckId = (DeckId) o;
        return Objects.equals(name, deckId.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
