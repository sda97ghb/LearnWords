package com.divanoapps.learnwords.data.api2;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class ApiUser {
    @SerializedName("email")
    private String email;

    @SerializedName("personalDecks")
    private List<String> personalDecks = new LinkedList<>();

    @SerializedName("sharedDecks")
    private List<ApiSharedDeckReference> sharedDecks = new LinkedList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPersonalDecks() {
        return personalDecks;
    }

    public void setPersonalDecks(List<String> personalDecks) {
        this.personalDecks = personalDecks;
    }

    public List<ApiSharedDeckReference> getSharedDecks() {
        return sharedDecks;
    }

    public void setSharedDecks(List<ApiSharedDeckReference> sharedDecks) {
        this.sharedDecks = sharedDecks;
    }
}
