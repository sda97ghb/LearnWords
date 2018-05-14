package com.divanoapps.learnwords.data.remote;

/**
 * Created by dmitry on 13.05.18.
 */

public class SyncCardId {
    private String deckName;
    private String word;
    private String comment;

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
