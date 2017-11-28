package com.divanoapps.learnwords.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by dmitry on 30.10.17.
 */

public class PictureId implements Serializable {
    private String mDeckName;
    private String mWord;
    private String mWordComment;

    public PictureId(String deckName, String word, String wordComment) {
        mDeckName = deckName;
        mWord = word;
        mWordComment = wordComment;
    }

    public String getDeckName() {
        return mDeckName;
    }

    public String getWord() {
        return mWord;
    }

    public String getWordComment() {
        return mWordComment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PictureId) {
            PictureId other = (PictureId) obj;
            return other.getDeckName().equals(getDeckName()) &&
                    other.getWord().equals(getWord()) &&
                    other.getWordComment().equals(getWordComment());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeckName(), getWord(), getWordComment());
    }
}
