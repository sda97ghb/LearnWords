package com.divanoapps.learnwords.data.api2;

import com.google.gson.annotations.SerializedName;

public class ApiCard {
    @SerializedName("owner")
    private String owner;

    @SerializedName("deck")
    private String deck;

    @SerializedName("word")
    private String word;

    @SerializedName("comment")
    private String comment;

    @SerializedName("translation")
    private String translation;

    @SerializedName("difficulty")
    private int difficulty;

    @SerializedName("hidden")
    private boolean hidden;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
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

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
