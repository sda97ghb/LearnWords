package com.divanoapps.learnwords.data.wombat;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.local.Syncable;

/**
 * Created by dmitry on 14.05.18.
 */

public interface EntityToIdConverter<T extends Syncable, I extends Id> {
    @NonNull
    I getId(T entity);

    Class<T> getEntityClass();
}
