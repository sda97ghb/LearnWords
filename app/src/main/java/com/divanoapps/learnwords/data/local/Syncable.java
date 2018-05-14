package com.divanoapps.learnwords.data.local;

/**
 * Created by dmitry on 14.05.18.
 */

public interface Syncable {
    Integer getSync();
    Long getTimestamp();

    void setSync(Integer sync);
    void setTimestamp(Long timestamp);
}
