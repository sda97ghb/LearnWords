package com.divanoapps.learnwords.data.remote;

import com.divanoapps.learnwords.data.Specification;

/**
 * Created by dmitry on 13.05.18.
 */

public class SyncCardIdSpecification implements Specification {
    private SyncCardId syncCardId;

    SyncCardIdSpecification(String deckName, String word, String comment) {
        syncCardId = new SyncCardId();
        syncCardId.setDeckName(deckName);
        syncCardId.setWord(word);
        syncCardId.setComment(comment);
    }

    public SyncCardId getSyncCardId() {
        return syncCardId;
    }

    public void setSyncCardId(SyncCardId syncCardId) {
        this.syncCardId = syncCardId;
    }
}
