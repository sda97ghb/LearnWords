package com.divanoapps.learnwords.YandexDictionary;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Definition {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("pos")
    @Expose
    private String partOfSpeech;

    @SerializedName("tr")
    @Expose
    private List<Translation> translations = null;

    public String getText() {
        return text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(partOfSpeech, translations);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof Definition))
            return false;
        Definition rhs = ((Definition) other);
        return translations.equals(rhs.translations) &&
                partOfSpeech.equals(rhs.partOfSpeech);
    }
}
