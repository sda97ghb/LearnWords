package com.divanoapps.learnwords.data.wombat;

/**
 * Created by dmitry on 14.05.18.
 */

public class Remote<I extends Id> {
    private I id;
    private long timestamp;

    public Remote() {
    }

    public Remote(I id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
