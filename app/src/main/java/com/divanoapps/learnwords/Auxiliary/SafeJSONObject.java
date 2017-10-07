package com.divanoapps.learnwords.Auxiliary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dmitry on 08.10.17.
 */

public class SafeJSONObject {
    JSONObject mObject;

    public SafeJSONObject(JSONObject object) {
        mObject = object;
    }

    public String getString(String name) {
        return getString(name, "");
    }

    public String getString(String name, String defaultValue) {
        try {
            return mObject.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        try {
            return mObject.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        try {
            return mObject.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public void put(String name, String value) {
        try {
            mObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String name, int value) {
        try {
            mObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(String name, boolean value) {
        try {
            mObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJSONObject() {
        return mObject;
    }
}
