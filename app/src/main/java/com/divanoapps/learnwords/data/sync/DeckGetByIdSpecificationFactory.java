package com.divanoapps.learnwords.data.sync;

import android.support.annotation.NonNull;

import com.divanoapps.learnwords.data.Specification;
import com.divanoapps.learnwords.data.local.DeckSpecificationsFactory;
import com.divanoapps.learnwords.data.wombat.GetByIdSpecificationFactory;

/**
 * Created by dmitry on 14.05.18.
 */

public class DeckGetByIdSpecificationFactory implements GetByIdSpecificationFactory<DeckId> {
    @NonNull
    @Override
    public Specification create(@NonNull DeckId id) {
        return DeckSpecificationsFactory.byName(id.getName());
    }
}
