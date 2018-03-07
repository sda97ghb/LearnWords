package com.divanoapps.learnwords.YandexDictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Mean {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Mean) == false) {
            return false;
        }
        Mean rhs = ((Mean) other);
        return text.equals(rhs.text);
    }

}
