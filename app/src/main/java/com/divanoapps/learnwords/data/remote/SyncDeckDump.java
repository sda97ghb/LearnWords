package com.divanoapps.learnwords.data.remote;

/**
 * Created by dmitry on 13.05.18.
 */

public class SyncDeckDump {
    private String name;
    private long timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
