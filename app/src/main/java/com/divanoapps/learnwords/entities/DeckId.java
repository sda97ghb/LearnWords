package com.divanoapps.learnwords.entities;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

/**
 * Created by dmitry on 30.10.17.
 */

public class DeckId implements Serializable {
    private String mName;

    public DeckId(String deckName) {
        mName = deckName;
    }

    public static DeckId fromJson(@NonNull JSONObject json) throws JSONException {
        return new DeckId(json.getString("name"));
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject() {{
            put("name", getName());
        }};
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeckId) {
            DeckId other = (DeckId) obj;
            return other.getName().equals(getName());
        }

        if (obj instanceof Deck)
            return equals(((Deck) obj).getId());

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    public String toQuery() {
        try {
            return "name=" + URLEncoder.encode(getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
