package com.divanoapps.learnwords.YandexDictionary;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("mean")
    @Expose
    private List<Mean> means = null;

    public String getText() {
        return text;
    }

    public List<Mean> getMeans() {
        return means;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, means);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Translation) == false) {
            return false;
        }
        Translation rhs = ((Translation) other);
        return text.equals(rhs.text) &&
                means.equals(rhs.means);
    }
}
