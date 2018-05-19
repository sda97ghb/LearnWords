package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.local.Syncable;

/**
 * Created by dmitry on 19.05.18.
 */

/**
 * Produces array of entities E.
 * @param <E> Entity class extended from Syncable.
 */
public interface ArrayFactory<E extends Syncable> {
    /**
     * Produces array of size {@code size}.
     * @param size Size of array
     * @return Array of entities.
     */
    E[] createArray(int size);

    /**
     * Produces array of single element.
     * @param obj Element of the created array.
     * @return Array of single element.
     */
    E[] singleton(E obj);
}
