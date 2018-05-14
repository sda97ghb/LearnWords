package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.local.Syncable;

/**
 * Created by dmitry on 14.05.18.
 */

public class RemoteFactory<T extends Syncable> {
    private EntityToIdConverter<T> entityToIdConverter;

    public RemoteFactory(EntityToIdConverter<T> entityToIdConverter) {
        this.entityToIdConverter = entityToIdConverter;
    }

    public Remote create(T entity) {
        Remote remote = new Remote();
        remote.setId(entityToIdConverter.getId(entity));
        remote.setTimestamp(entity.getTimestamp());
        return remote;
    }
}
