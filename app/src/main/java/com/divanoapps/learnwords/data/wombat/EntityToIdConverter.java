package com.divanoapps.learnwords.data.wombat;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.local.Syncable;

/**
 * Created by dmitry on 14.05.18.
 */

/**
 * Creates ids for entities.
 * @param <E> Entity class extended from Syncable
 * @param <I> Id class extended from Id
 */
public interface EntityToIdConverter<E extends Syncable, I extends Id> {
    /**
     * Creates id for entity.
     * @param entity Entity object
     * @return Id for entity.
     */
    @NonNull
    I getId(@NonNull E entity);

    /**
     * Class of entity for which this converter is implemented.
     * @return Class of entity as {@code ConcreteEntity.class}.
     */
    @NonNull
    Class<E> getEntityClass();
}
