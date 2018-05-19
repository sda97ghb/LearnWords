package com.divanoapps.learnwords.data.wombat;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.Specification;

/**
 * Returns getById specification for local repository.
 * @param <T> Concrete Id class.
 */
public interface GetByIdSpecificationFactory<T extends Id> {
    /**
     * Returns getById specification for local repository.
     * @param id Id of the entity that's need to be fetched from the local repository.
     * @return getById specification for local repository.
     */
    @NonNull
    Specification create(@NonNull T id);
}
