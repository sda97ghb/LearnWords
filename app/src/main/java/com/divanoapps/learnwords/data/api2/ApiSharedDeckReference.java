package com.divanoapps.learnwords.data.api2;

import com.google.gson.annotations.SerializedName;

public class ApiSharedDeckReference {
    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public ApiSharedDeckReference() {
    }

    public ApiSharedDeckReference(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
