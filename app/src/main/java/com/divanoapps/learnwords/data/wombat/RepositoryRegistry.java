package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.Repository;

import java.util.Map;

/**
 * Created by dmitry on 14.05.18.
 */

public class RepositoryRegistry {
    private Map<Class, Repository> repositories;

    public <T> void addRepository(Class<T> klass, Repository<T> repository) {
        repositories.put(klass, repository);
    }

    @SuppressWarnings("unchecked")
    public <T> Repository<T> getRepository(Class<T> klass) {
        return (Repository<T>) repositories.get(klass);
    }
}
