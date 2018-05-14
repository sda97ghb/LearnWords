package com.divanoapps.learnwords.data.wombat;

/**
 * Created by dmitry on 14.05.18.
 */

public class Wombat {
    private EntityToIdConverterRegistry entityToIdConverterRegistry;
    private RepositoryRegistry repositoryRegistry;

    public EntityToIdConverterRegistry getEntityToIdConverterRegistry() {
        return entityToIdConverterRegistry;
    }

    public void setEntityToIdConverterRegistry(EntityToIdConverterRegistry registry) {
        this.entityToIdConverterRegistry = registry;
    }

    public void setRepositoryRegistry(RepositoryRegistry registry) {
        this.repositoryRegistry = registry;
    }
}
