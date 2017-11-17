package com.divanoapps.learnwords.entities;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.auxiliary.SafeJSONObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents single card that is immutable.
 */
public class Card {
    private String  mDeckName;
    private String  mWord;
    private String  mWordComment;
    private String  mTranslation;
    private String  mTranslationComment;
    private int     mDifficulty;
    private boolean mIsHidden;
    private String  mPictureUrl;
    private boolean mCropPicture;

    public Card() {
        mDeckName           = "";
        mWord               = "";
        mWordComment        = "";
        mTranslation        = "";
        mTranslationComment = "";
        mDifficulty         = getDefaultDifficulty();
        mIsHidden           = false;
        mPictureUrl         = "";
        mCropPicture        = false;
    }

    public Card(Card other) {
        mDeckName           = other.getDeckName();
        mWord               = other.getWord();
        mWordComment        = other.getWordComment();
        mTranslation        = other.getTranslation();
        mTranslationComment = other.getTranslationComment();
        mDifficulty         = other.getDifficulty();
        mIsHidden           = other.isHidden();
        mPictureUrl         = other.getPictureUrl();
        mCropPicture        = other.cropPicture();
    }

    static Card fromJson(@NonNull String deckName, @NonNull String json) throws JSONException {
        return fromJson(deckName, new JSONObject(json));
    }

    static Card fromJson(@NonNull String deckName, @NonNull JSONObject jsonObject) {
        SafeJSONObject safeJSONObject = new SafeJSONObject(jsonObject);

        int difficulty = safeJSONObject.getInt("difficulty", Card.getDefaultDifficulty());
        if (difficulty > Card.getMaxDifficulty())
            difficulty = Card.getMaxDifficulty();
        if (difficulty < Card.getMinDifficulty())
            difficulty = Card.getMinDifficulty();

        return new Card.Builder()
                .setDeckName(          deckName)
                .setWord(              safeJSONObject.getString ("word"))
                .setWordComment(       safeJSONObject.getString ("wordComment"))
                .setTranslation(       safeJSONObject.getString ("translation"))
                .setTranslationComment(safeJSONObject.getString ("translationComment"))
                .setDifficulty(        difficulty)
                .setHidden(            safeJSONObject.getBoolean("isHidden"))
                .setPictureUrl(        "")
                .setCropPicture(       safeJSONObject.getBoolean("cropPicture"))
                .build();
    }

    public JSONObject toJson() {
        SafeJSONObject safeJSONObject = new SafeJSONObject(new JSONObject());
        safeJSONObject.put("word",               getWord());
        safeJSONObject.put("wordComment",        getWordComment());
        safeJSONObject.put("translation",        getTranslation());
        safeJSONObject.put("translationComment", getTranslationComment());
        safeJSONObject.put("difficulty",         getDifficulty());
        safeJSONObject.put("isHidden",           isHidden());
        safeJSONObject.put("cropPicture",        cropPicture());
        return safeJSONObject.getInternalJSONObject();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Card))
            return false;
        Card other = (Card) obj;
        return  getDeckName()          .equals(other.getDeckName())           &&
                getWord()              .equals(other.getWord())               &&
                getWordComment()       .equals(other.getWordComment())        &&
                getTranslation()       .equals(other.getTranslation())        &&
                getTranslationComment().equals(other.getTranslationComment()) &&
                getDifficulty()         ==     other.getDifficulty()          &&
                isHidden()              ==     other.isHidden()               &&
                getPictureUrl()        .equals(other.getPictureUrl())         &&
                cropPicture()           ==     other.cropPicture();
    }

    @Override
    public String toString() {
        return "Card {" +
                "deckName"           + "=" + getDeckName()           + ", " +
                "word"               + "=" + getWord()               + ", " +
                "wordComment"        + "=" + getWordComment()        + ", " +
                "translation"        + "=" + getTranslation()        + ", " +
                "translationComment" + "=" + getTranslationComment() + ", " +
                "difficulty"         + "=" + getDifficulty()         + ", " +
                "isHidden"           + "=" + isHidden()              + ", " +
                "pictureUrl"         + "=" + getPictureUrl()         + ", " +
                "cropPicture"        + "=" + cropPicture()           +
                "}";
    }

    public CardId getId() {
        return new CardId(mDeckName, mWord, mWordComment);
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

    public boolean hasWordComment() { return !(mWordComment.trim().isEmpty()); }

    public String getTranslation() {
        return mTranslation;
    }

    public String getTranslationComment() {
        return mTranslationComment;
    }

    public boolean hasTranslationComment() { return !(mTranslationComment.trim().isEmpty()); }

    public static int getMinDifficulty() {
        return 0;
    }

    public static int getMaxDifficulty() {
        return 30;
    }

    public static int getDefaultDifficulty() {
        return 10;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public boolean hasPicture() {
        return !mPictureUrl.trim().equals("");
    }

    public boolean cropPicture() {
        return mCropPicture;
    }

    public static class Builder {
        private Card mCard;

        public Builder() {
            mCard = new Card();
        }

        public Builder(Card other) {
            mCard = new Card(other);
        }

        public Builder setDeckName(String deckName) {
            mCard.mDeckName = deckName;
            return this;
        }

        public Builder setWord(String word) {
            mCard.mWord = word;
            return this;
        }

        public Builder setWordComment(String wordComment) {
            mCard.mWordComment = wordComment;
            return this;
        }

        public Builder setTranslation(String translation) {
            mCard.mTranslation = translation;
            return this;
        }

        public Builder setTranslationComment(String translationComment) {
            mCard.mTranslationComment = translationComment;
            return this;
        }

        public Builder setHidden(boolean isHidden) {
            mCard.mIsHidden = isHidden;
            return this;
        }

        public Builder setDifficulty(int difficulty) {
            mCard.mDifficulty = difficulty;
            return this;
        }

        public Builder setPictureUrl(String pictureUrl) {
            mCard.mPictureUrl = pictureUrl;
            return this;
        }

        public Builder setCropPicture(boolean cropPicture) {
            mCard.mCropPicture = cropPicture;
            return this;
        }

        public Card build() {
            return new Card(mCard);
        }
    }
}
