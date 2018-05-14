package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.Specification;

/**
 * Created by dmitry on 13.05.18.
 */

public class SyncDeckNameSpecification implements Specification {
    private String deckName;

    public SyncDeckNameSpecification(String deckName) {
        this.deckName = deckName;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }
}
