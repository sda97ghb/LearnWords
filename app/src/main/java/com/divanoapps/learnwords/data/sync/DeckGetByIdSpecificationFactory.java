package com.divanoapps.learnwords.data.sync;

import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.wombat.GetByIdSpecificationFactory;

/**
 * Created by dmitry on 14.05.18.
 */

public class DeckGetByIdSpecificationFactory implements GetByIdSpecificationFactory<DeckId> {
    @Override
    public Specification create(DeckId id) {
        return DeckSpecificationsFactory.byName(id.getName());
    }
}
