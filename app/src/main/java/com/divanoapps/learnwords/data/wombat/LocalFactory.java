package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.local.Syncable;

/**
 * Created by dmitry on 14.05.18.
 */

public class LocalFactory<T extends Syncable, I extends Id> {
    private EntityToIdConverter<T, I> entityToIdConverter;

    public LocalFactory(EntityToIdConverter<T, I> entityToIdConverter) {
        this.entityToIdConverter = entityToIdConverter;
    }

    public Local<I> create(T entity) {
        Local<I> local = new Local<>();
        local.setId(entityToIdConverter.getId(entity));
        local.setSync(entity.getSync());
        local.setTimestamp(entity.getTimestamp());
        return local;
    }
}
