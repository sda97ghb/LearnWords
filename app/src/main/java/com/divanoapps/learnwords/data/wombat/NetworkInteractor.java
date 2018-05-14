package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.local.Syncable;

import java.util.List;

/**
 * Created by dmitry on 14.05.18.
 */

public interface NetworkInteractor<T extends Syncable, I extends Id> {
    List<Remote<I>> getRemotes(String entity);
    T download(I id);
    void upload(T obj);
    void deleteOnServer(I id);
}
