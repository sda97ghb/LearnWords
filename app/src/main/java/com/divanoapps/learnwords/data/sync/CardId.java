package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.wombat.Id;

import java.util.Objects;

/**
 * Created by dmitry on 14.05.18.
 */

public class CardId implements Id {
    private String deckName;
    private String word;
    private String comment;

    public CardId() {
    }

    public CardId(String deckName, String word, String comment) {
        this.deckName = deckName;
        this.word = word;
        this.comment = comment;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardId cardId = (CardId) o;
        return Objects.equals(deckName, cardId.deckName) &&
            Objects.equals(word, cardId.word) &&
            Objects.equals(comment, cardId.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckName, word, comment);
    }
}
