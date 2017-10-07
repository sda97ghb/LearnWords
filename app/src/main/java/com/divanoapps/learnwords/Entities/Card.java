package com.divanoapps.learnwords.Entities;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.Auxiliary.SafeJSONObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents single card.
 */
public class Card {
    private String mDeckName;
    private String mWord;
    private String mWordComment;
    private String mTranslation;
    private String mTranslationComment;
    private int mDifficulty;
    private boolean mIsHidden;
    private String mPictureUrl;
    private boolean mCropPicture;

    public Card() {
        mDeckName = "";
        mWord = "";
        mWordComment = "";
        mTranslation = "";
        mTranslationComment = "";
        mDifficulty = getDefaultDifficulty();
        mIsHidden = false;
        mPictureUrl = "";
        mCropPicture = false;
    }

    public Card(Card other) {
        setDeckName(          other.getDeckName());
        setWord(              other.getWord());
        setWordComment(       other.getWordComment());
        setTranslation(       other.getTranslation());
        setTranslationComment(other.getTranslationComment());
        setDifficulty(        other.getDifficulty());
        setIsHidden(          other.isHidden());
        setPictureUrl(        other.getPictureUrl());
        setCropPicture(       other.cropPicture());
    }

    static Card fromJson(@NonNull String deckName, @NonNull String json) throws JSONException {
        return fromJson(deckName, new JSONObject(json));
    }

    static Card fromJson(@NonNull String deckName, @NonNull JSONObject jsonObject) {
        SafeJSONObject safeJSONObject = new SafeJSONObject(jsonObject);
        Card card = new Card();
        card.setDeckName(deckName);
        card.setWord(              safeJSONObject.getString ("word"));
        card.setWordComment(       safeJSONObject.getString ("wordComment"));
        card.setTranslation(       safeJSONObject.getString ("translation"));
        card.setTranslationComment(safeJSONObject.getString ("translationComment"));
        card.setDifficulty(        safeJSONObject.getInt    ("difficulty", Card.getDefaultDifficulty()));
        if (card.getDifficulty() > Card.getMaxDifficulty())
            card.setDifficulty(Card.getMaxDifficulty());
        if (card.getDifficulty() < Card.getMinDifficulty())
            card.setDifficulty(Card.getMinDifficulty());
        card.setIsHidden(          safeJSONObject.getBoolean("isHidden"));
        card.setPictureUrl("");
        card.setCropPicture(       safeJSONObject.getBoolean("cropPicture"));
        return card;
    }

    public JSONObject toJson() {
        SafeJSONObject safeJSONObject = new SafeJSONObject(new JSONObject());
        safeJSONObject.put("word", getWord());
        safeJSONObject.put("wordComment", getWordComment());
        safeJSONObject.put("translation", getTranslation());
        safeJSONObject.put("translationComment", getTranslationComment());
        safeJSONObject.put("difficulty", getDifficulty());
        safeJSONObject.put("isHidden", isHidden());
        safeJSONObject.put("cropPicture", cropPicture());
        return safeJSONObject.getJSONObject();
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

    public String getDeckName() {
        return mDeckName;
    }

    public void setDeckName(@NonNull String deckName) {
        mDeckName = deckName;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(@NonNull String word) {
        mWord = word;
    }

    public String getWordComment() {
        return mWordComment;
    }

    public void setWordComment(@NonNull String wordComment) {
        mWordComment = wordComment;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(@NonNull String translation) {
        mTranslation = translation;
    }

    public String getTranslationComment() {
        return mTranslationComment;
    }

    public void setTranslationComment(@NonNull String translationComment) {
        mTranslationComment = translationComment;
    }

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

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
    }

    public void increaseDifficulty() {
        if (mDifficulty < getMaxDifficulty())
            ++ mDifficulty;
    }

    public void decreaseDifficulty() {
        if (mDifficulty > getMinDifficulty())
            -- mDifficulty;
    }

    public void resetDifficulty() {
        mDifficulty = getDefaultDifficulty();
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    public void setIsHidden(boolean isHidden) {
        mIsHidden = isHidden;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public boolean hasPicture() {
        return !mPictureUrl.trim().equals("");
    }

    public boolean cropPicture() {
        return mCropPicture;
    }

    public void setCropPicture(boolean cropPicture) {
        mCropPicture = cropPicture;
    }
}
