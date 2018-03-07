package com.divanoapps.learnwords.YandexDictionary;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class DictionaryResult {

    @SerializedName("def")
    @Expose
    private List<Definition> definitions = null;

    public List<Definition> getDefinitions() {
        return definitions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(definitions);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DictionaryResult)) {
            return false;
        }
        DictionaryResult rhs = ((DictionaryResult) other);
        return definitions.equals(rhs.definitions);
    }

}
