package com.divanoapps.learnwords.data.wombat;

import com.divanoapps.learnwords.data.Specification;

/**
 * Created by dmitry on 14.05.18.
 */

public interface GetByIdSpecificationFactory<T extends Id> {
    Specification create(T id);
}
