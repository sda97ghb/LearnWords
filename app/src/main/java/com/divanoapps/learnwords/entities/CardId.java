package com.divanoapps.learnwords.entities;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Created by dmitry on 30.10.17.
 */

public class CardId implements Serializable {
    private String mDeckName;
    private String mWord;
    private String mWordComment;

    public CardId(String deckName, String word, String wordComment) {
        mDeckName = deckName;
        mWord = word;
        mWordComment = wordComment;
    }

    public static CardId fromJson(@NonNull JSONObject json) throws JSONException {
        return new CardId(json.getString("deck"),
                          json.getString("word"),
                          json.getString("comment"));
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject() {{
            put("deck", getDeckName());
            put("word", getWord());
            put("comment", getWordComment());
        }};
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
        if (obj instanceof CardId) {
            CardId other = (CardId) obj;
            return other.getDeckName().equals(getDeckName()) &&
                    other.getWord().equals(getWord()) &&
                    other.getWordComment().equals(getWordComment());
        }

        if (obj instanceof Card)
            return equals(((Card) obj).getId());

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeckName(), getWord(), getWordComment());
    }

    public String toQuery() {
        try {
            return "deck="    + URLEncoder.encode(getDeckName(), "UTF-8")   + "&" +
                   "word="    + URLEncoder.encode(getWord(), "UTF-8")       + "&" +
                   "comment=" + URLEncoder.encode(getWordComment(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
