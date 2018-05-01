package com.divanoapps.learnwords.data.api2;

import com.google.gson.annotations.SerializedName;

public class ApiDeckInfo {
    @SerializedName("owner")
    private String owner;

    @SerializedName("name")
    private String name;

    @SerializedName("fromLanguage")
    private String fromLanguage;

    @SerializedName("toLanguage")
    private String toLanguage;

    @SerializedName("cards")
    private int numberOfCards;

    @SerializedName("hidden")
    private int numberOfHiddenCards;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public int getNumberOfHiddenCards() {
        return numberOfHiddenCards;
    }

    public void setNumberOfHiddenCards(int numberOfHiddenCards) {
        this.numberOfHiddenCards = numberOfHiddenCards;
    }
}
