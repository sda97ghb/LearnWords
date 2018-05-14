package com.divanoapps.learnwords.data.wombat;

/**
 * Created by dmitry on 14.05.18.
 */

public class Local<I extends Id> {
    private I id;
    private long timestamp;
    private int sync;

    public Local() {
    }

    public Local(I id, long timestamp, int sync) {
        this.id = id;
        this.timestamp = timestamp;
        this.sync = sync;
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

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
}
